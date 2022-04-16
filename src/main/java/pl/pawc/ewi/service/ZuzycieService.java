package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ZuzycieService {

    private final ZuzycieRepository zuzycieRepository;
    private final StanRepository stanRepository;
    private final DokumentRepository dokumentRepository;

    public Double getSuma(long normaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException {

        Dokument dokument;

        if(excludedDocNumber != null){
            dokument = dokumentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException(excludedDocNumber));
            Calendar calD = Calendar.getInstance();
            calD.setTime(dokument.getData());
        }
        else {
            dokument = null;
        }

        Norma norma = new Norma();
        norma.setId(normaId);
        Calendar cal = Calendar.getInstance();

        List<Zuzycie> collect = zuzycieRepository.findByNorma(norma).stream()
                .filter(z -> {
                        cal.setTime(z.getDokument().getData());
                        return cal.get(Calendar.MONTH) + 1 == month
                            && cal.get(Calendar.YEAR) == year
                            && (dokument == null || !z.getDokument().getNumer().equals(excludedDocNumber))
                            && (dokument == null || dokument.getData().compareTo(z.getDokument().getData()) > 0);
                    }
                ).collect(Collectors.toList());

        Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(norma, year, month);

        if(stan == null){
            stan = new Stan();
            stan.setWartosc(0D);
        }

        double zatankowano = collect.stream().mapToDouble(Zuzycie::getZatankowano).sum();
        double ogrzewanie = collect.stream().mapToDouble(Zuzycie::getOgrzewanie).sum();

        double sum = collect.stream().mapToDouble(z ->
            myRound(z.getWartosc() * z.getNorma().getWartosc(), false)
        ).sum();

        return myRound(stan.getWartosc() + zatankowano - ogrzewanie - sum, false);

    }

    public Double getSumaYear(long normaId, int year){

        Norma norma = new Norma();
        norma.setId(normaId);
        Calendar cal = Calendar.getInstance();

        List<Zuzycie> collect = zuzycieRepository.findByNorma(norma).stream()
                .filter(z -> {
                            cal.setTime(z.getDokument().getData());
                            return cal.get(Calendar.YEAR) == year;
                        }
                ).collect(Collectors.toList());

        return collect.stream().mapToDouble(z ->
                myRound(z.getWartosc() * z.getNorma().getWartosc(), true)
        ).sum();

    }

    private double myRound(double d, boolean precisionMode){
        double r = (double) Math.round(d*100)/100;
        if(precisionMode){
            return r;
        }
        else{
            return (double) Math.round(r*10)/10;
        }
    }

}
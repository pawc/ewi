package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.NormaNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ZuzycieService {

    private final ZuzycieRepository zuzycieRepository;
    private final StanRepository stanRepository;
    private final DokumentRepository dokumentRepository;
    private final NormaRepository normaRepository;

    public BigDecimal getSuma(long normaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException, NormaNotFoundException {

        Dokument dokument;

        if(excludedDocNumber != null){
            dokument = dokumentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException(excludedDocNumber));
            Calendar calD = Calendar.getInstance();
            calD.setTime(dokument.getData());
        }
        else {
            dokument = null;
        }

        Norma norma = normaRepository.findById(normaId).orElse(null);
        if(norma == null) throw new NormaNotFoundException(normaId);
        Calendar cal = Calendar.getInstance();

        List<Zuzycie> collect = zuzycieRepository.findByNorma(norma).stream()
                .filter(z -> {
                        cal.setTime(z.getDokument().getData());
                        return cal.get(Calendar.MONTH) + 1 == month
                            && cal.get(Calendar.YEAR) == year
                            && (dokument == null || !z.getDokument().getNumer().equals(excludedDocNumber))
                            && (dokument == null || dokument.getData().compareTo(z.getDokument().getData()) > 0);
                    }
                ).toList();

        Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(norma, year, month);

        if(stan == null){
            stan = new Stan();
            stan.setWartosc(BigDecimal.ZERO);
        }

        BigDecimal zatankowano = collect.stream().map(Zuzycie::getZatankowano).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ogrzewanie = collect.stream().map(Zuzycie::getOgrzewanie).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sum = collect.stream()
                .map(z -> (z.getWartosc().multiply(z.getNorma().getWartosc())).setScale(norma.isCzyZaokr1setna() ? 2 : 1, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return (stan.getWartosc().add(zatankowano).subtract(ogrzewanie).subtract(sum)).setScale(norma.isCzyZaokr1setna() ? 2 : 1, RoundingMode.HALF_UP);

    }

    public BigDecimal getSumaYear(long normaId, int year){

        Norma norma = new Norma();
        norma.setId(normaId);
        Calendar cal = Calendar.getInstance();

        List<Zuzycie> collect = zuzycieRepository.findByNorma(norma).stream()
                .filter(z -> {
                            cal.setTime(z.getDokument().getData());
                            return cal.get(Calendar.YEAR) == year;
                        }
                ).toList();

        return collect.stream().map(z ->
                z.getWartosc().multiply(z.getNorma().getWartosc()).setScale(2, RoundingMode.HALF_UP)
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

}
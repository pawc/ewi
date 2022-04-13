package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ZuzycieService {

    private final ZuzycieRepository zuzycieRepository;
    private final StanRepository stanRepository;

    public Double getSuma(long normaId, int year, int month){

        Norma norma = new Norma();
        norma.setId(normaId);
        Calendar cal = Calendar.getInstance();

        List<Zuzycie> collect = zuzycieRepository.findByNorma(norma).stream()
                .filter(z -> {
                            cal.setTime(z.getDokument().getData());
                            return cal.get(Calendar.MONTH) + 1 == month && cal.get(Calendar.YEAR) == year;
                        }
                ).collect(Collectors.toList());

        Optional<Stan> stan = stanRepository.findByNormaAndRokAndMiesiac(norma, year, month).stream().findFirst();

        double stanD = stan.orElseGet(() -> {
            Stan s = new Stan();
            s.setWartosc(0D);
            return s;
        }).getWartosc();
        double zatankowano = collect.stream().mapToDouble(Zuzycie::getZatankowano).sum();
        double ogrzewanie = collect.stream().mapToDouble(Zuzycie::getOgrzewanie).sum();

        double sum = collect.stream().mapToDouble(z ->
            myRound(z.getWartosc() * z.getNorma().getWartosc())
        ).sum();

        return myRound(stanD + zatankowano - ogrzewanie - sum);

    }

    private double myRound(double d){
        double r = (double) Math.round(d*100)/100;
        return (double) Math.round(r*10)/10;
    }

}
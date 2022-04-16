package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.RaportStan;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StanService {

    private final StanRepository stanRepository;
    private final NormaRepository normaRepository;

    public List<RaportStan> findBy(int year, int month) {

        List<RaportStan> result = new ArrayList<>();

        normaRepository.findAll().forEach(n -> {
            RaportStan raportStan = new RaportStan();
            raportStan.setMaszynanazwa(n.getMaszyna().getNazwa());
            raportStan.setMaszynaid(n.getMaszyna().getId());
            raportStan.setJednostka(n.getJednostka());
            raportStan.setNormaid(n.getId());
            Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(n, year, month);
            if(stan == null) {
                stan = new Stan();
                stan.setId(-1);
                stan.setWartosc(0D);
            }
            raportStan.setStanid(stan.getId());
            raportStan.setStanpoczatkowy(stan.getWartosc());
            result.add(raportStan);
        });

        return result;

    }

}
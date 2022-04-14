package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.repository.KilometryRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RaportService {

    private final KilometryRepository kilometryRepository;
    private final MaszynaService maszynaService;

    List<RaportKilometry> getKilometryRaport(int rok, int miesiac){

        List<Maszyna> allActive = maszynaService.findAllActive();
        List<RaportKilometry> result = new ArrayList<>();

        allActive.forEach(m -> {
            RaportKilometry raportKilometry = new RaportKilometry();
            raportKilometry.setMaszynaid(m.getId());
            raportKilometry.setMaszynanazwa(m.getNazwa());
            Kilometry oneByMaszynaAndRokAndMiesiac = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(m, rok, miesiac);
            if (oneByMaszynaAndRokAndMiesiac == null){
                raportKilometry.setStanpoczatkowy(0);
            }
            else{
                raportKilometry.setStanpoczatkowy(oneByMaszynaAndRokAndMiesiac.getWartosc());
            }
            result.add(raportKilometry);
        });

        return result;

    }

}
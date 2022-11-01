package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.RaportStan;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StanService {

    private static final Logger logger = LogManager.getLogger(StanService.class);
    private final StanRepository stanRepository;
    private final NormaRepository normaRepository;

    public List<RaportStan> findBy(int year, int month) {

        List<RaportStan> result = new ArrayList<>();

        normaRepository.findAll().forEach(n -> {
            RaportStan raportStan = new RaportStan();
            raportStan.setMaszynanazwa(n.getMaszyna().getNazwa());
            raportStan.setMaszynaid(n.getMaszyna().getId());
            String jednostka = n.getJednostkaObj() == null ? n.getJednostka() : n.getJednostkaObj().getNazwa();
            raportStan.setJednostka(jednostka);
            raportStan.setNormaid(n.getId());
            Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(n, year, month);
            if(stan == null) {
                stan = new Stan();
                stan.setId(-1);
                stan.setWartosc(BigDecimal.ZERO);
            }
            raportStan.setStanid(stan.getId());
            raportStan.setStanpoczatkowy(stan.getWartosc());
            result.add(raportStan);
        });

        return result;

    }

    public boolean post(Stan stan){
        Stan stanDB = stanRepository.findOneByNormaAndRokAndMiesiac(stan.getNorma(), stan.getRok(), stan.getMiesiac());
        if(stanDB == null){
            stanRepository.save(stan);
            return true;
        }
        else{
            stanDB.setWartosc(stan.getWartosc());
            stanRepository.save(stanDB);
            return false;
        }
    }

    public void stanyPost(List<Stan> stany){
        List<Stan> byParams;
        Stan stanDB;
        for(Stan stan : stany){

            Optional<Norma> byId = normaRepository.findById(stan.getNorma().getId());
            if(byId.isPresent()){
                if(!byId.get().getMaszyna().isPrzenoszonaNaKolejnyOkres()) continue;
            }

            stanDB = stanRepository.findOneByNormaAndRokAndMiesiac(stan.getNorma(), stan.getRok(), stan.getMiesiac());
            if(stanDB == null){
                stanRepository.save(stan);
            }
            else{
                stanDB.setWartosc(stan.getWartosc());
                stanRepository.save(stanDB);
            }
        }
    }

}
package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.repository.KilometryRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KilometryService {

    private static final Logger logger = LogManager.getLogger(KilometryService.class);
    private final KilometryRepository kilometryRepository;
    private final MaszynaService maszynaService;

    public boolean post(Kilometry kilometry){
        Kilometry kilometryDB = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(kilometry.getMaszyna(), kilometry.getRok(), kilometry.getMiesiac());

        if(kilometryDB == null){
            kilometryRepository.save(kilometry);
            return true;
        }
        else{
            kilometryDB.setWartosc(kilometry.getWartosc());
            kilometryRepository.save(kilometryDB);
            return false;
        }
    }

    public void post(List<Kilometry> kilometry){
        for(Kilometry km : kilometry){
            Optional<Maszyna> byId = maszynaService.findById(km.getMaszyna().getId());
            if(byId.isPresent() && !byId.get().isPrzenoszonaNaKolejnyOkres()) continue;

            Kilometry kilometryDB = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(km.getMaszyna(), km.getRok(), km.getMiesiac());
            if(kilometryDB == null){
                logger.info(" /kilometryList POST dodano {}", km);
                kilometryRepository.save(km);
            }
            else{
                logger.info(" /kilometryList POST zaktualizowano {}", km);
                kilometryDB.setWartosc(km.getWartosc());
                kilometryRepository.save(kilometryDB);
            }
        }
    }

}
package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.repository.KilometersRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KilometersService {

    private static final Logger logger = LogManager.getLogger(KilometersService.class);
    private final KilometersRepository kilometersRepository;
    private final MachineService machineService;

    public boolean post(Kilometers kilometers){
        Kilometers kilometersDB = kilometersRepository.findOneByMachineAndYearAndMonth(kilometers.getMachine(), kilometers.getYear(), kilometers.getMonth());

        if(kilometersDB == null){
            kilometersRepository.save(kilometers);
            return true;
        }
        else{
            kilometersDB.setValue(kilometers.getValue());
            kilometersRepository.save(kilometersDB);
            return false;
        }
    }

    public void post(List<Kilometers> kilometers){
        for(Kilometers km : kilometers){
            Optional<Machine> byId = machineService.findById(km.getMachine().getId());
            if(byId.isPresent() && !byId.get().isCarriedOver()) continue;

            Kilometers kilometersDB = kilometersRepository.findOneByMachineAndYearAndMonth(km.getMachine(), km.getYear(), km.getMonth());
            if(kilometersDB == null){
                logger.info("/kilometryList POST dodano {}", km);
                kilometersRepository.save(km);
            }
            else{
                logger.info("/kilometryList POST zaktualizowano {}", km);
                kilometersDB.setValue(km.getValue());
                kilometersRepository.save(kilometersDB);
            }
        }
    }

}
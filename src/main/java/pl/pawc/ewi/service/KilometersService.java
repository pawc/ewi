package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.repository.KilometersRepository;
import java.util.List;

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
            logger.info("/kilometers POST added {}", kilometers);
            return true;
        }
        else{
            kilometersDB.setValue(kilometers.getValue());
            kilometersRepository.save(kilometersDB);
            logger.info("/kilometers POST updated {}", kilometers);
            return false;
        }
    }

    public void post(List<Kilometers> kilometers){
        for(Kilometers km : kilometers){
            if(machineService.isCarriedOver(km.getMachine().getId())) post(km);
        }
    }

}
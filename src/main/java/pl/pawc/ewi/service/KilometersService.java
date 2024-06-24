package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.repository.KilometersRepository;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KilometersService {

    private static final Logger logger = LogManager.getLogger(KilometersService.class);
    private final KilometersRepository kilometersRepository;
    private final MachineService machineService;

    /**
     * Post kilometers for a machine for a given year and month.
     * @param kilometers kilometers
     * @return true if kilometers were added, false if they were updated
     */
    public boolean post(Kilometers kilometers){
        Optional<Kilometers> kilometersByMachineAndYearAndMonth = kilometersRepository
                .findOneByMachineAndYearAndMonth(kilometers.getMachine(), kilometers.getYear(), kilometers.getMonth());

        if (kilometersByMachineAndYearAndMonth.isPresent()) {
            Kilometers kilometersDB = kilometersByMachineAndYearAndMonth.get();
            kilometersDB.setValue(kilometers.getValue());
            logger.info("/kilometers POST updated {}", kilometers);
            return false;
        }
        else {
            kilometersRepository.save(kilometers);
            logger.info("/kilometers POST added {}", kilometers);
            return true;
        }
    }

    /**
     * Post kilometers for machines that are carried over from previous month.
     * @param kilometers list of kilometers
     */
    public void post(List<Kilometers> kilometers){
        kilometers.stream()
                .filter(this::isCarriedOver)
                .forEach(this::post);
    }

    /**
    * Check if machine is carried over from previous month.
    * @param kilometers kilometers
     */
    private boolean isCarriedOver(Kilometers kilometers) {
        return machineService.isCarriedOver(kilometers.getMachine().getId());
    }

}
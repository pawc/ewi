package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.repository.FuelConsumptionStandardRepository;
import pl.pawc.ewi.repository.MachineRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MachineService {
    
    private final MachineRepository machineRepository;
    private final FuelConsumptionStandardRepository fuelConsumptionStandardRepository;
    private final FuelConsumptionService fuelConsumptionService;
    private final DocumentService documentService;
    private final CategoryService categoryService;

    public List<Machine> findAllActive(){
        List<Machine> activeMachines = machineRepository.findByIsActiveTrue();
        for (Machine m : activeMachines)
            if (!m.getCategories().isEmpty()) m.getCategories().forEach(c -> c.setMachines(null));

        return activeMachines;

    }

    public List<Machine> findAll(){
        return machineRepository.findAll();
    }

    public List<Machine> findAllUncategorized(){
        return machineRepository.findAll().stream().
                filter(m -> m.getCategories().isEmpty())
                .toList();
    }

    public Optional<Machine> findById(String id){
        Optional<Machine> byId = machineRepository.findById(id);
        byId.ifPresent(m -> m.getCategories().forEach(c -> c.setMachines(null)));
        return byId;
    }

    public boolean isCarriedOver(String id){
        return machineRepository.findById(id)
                .map(Machine::isCarriedOver)
                .orElse(false);
    }

    @SneakyThrows
    public Machine get(String id, String miesiac){
        Optional<Machine> result = machineRepository.findById(id);
        Machine machine = new Machine();
        if(result.isPresent()){

            machine = result.get();
            List<FuelConsumptionStandard> fuelConsumptionStandardsByMachine = fuelConsumptionStandardRepository.findByMachine(machine);
            machine.setFuelConsumptionStandards(fuelConsumptionStandardsByMachine);
            machine.getCategories().forEach(c -> c.setMachines(null));

            if(miesiac != null){
                try{
                    int year = Integer.parseInt(miesiac.split("-")[0]);
                    int month = Integer.parseInt(miesiac.split("-")[1]);

                    BigDecimal sumKilometers = documentService.getSumKilometers(machine.getId(), year, month, null);
                    machine.setSumOfKilometers(sumKilometers);

                    for(FuelConsumptionStandard fuelConsumptionStandard : fuelConsumptionStandardsByMachine){
                        BigDecimal suma = fuelConsumptionService.getSum(fuelConsumptionStandard.getId(), year, month, null);
                        fuelConsumptionStandard.setSum(suma);
                    }
                }
                catch(NumberFormatException e){
                    // skip
                }
            }

            return machine;
        }
        return machine;
    }

    public Machine post(Machine machine){

        Set<Category> categories = new HashSet<>();

        // new categories can't be added with a new machine anyway
        if (machine.getCategories() != null) {
            machine.getCategories().forEach(c -> {
                    Category oneByName = categoryService.findOneByName(c.getName());
                    oneByName.getMachines().add(machine);
                    categories.add(oneByName);
                }
            );
        }

        machine.setCategories(categories);

        machine.getFuelConsumptionStandards().forEach(n -> {
            n.setMachine(machine);
            fuelConsumptionStandardRepository.save(n);
        });

        return machine;
    }

    public void put(Machine machine){
        Optional<Machine> byId = machineRepository.findById(machine.getId());
        if(byId.isPresent()) {

            Machine machineDB = byId.get();
            machineDB.setName(machine.getName());
            machineDB.setActive(machine.isActive());
            machineDB.setDescription(machine.getDescription());

            Set<Category> categories = new HashSet<>();

            if(machine.getCategories() != null){
                machine.getCategories().forEach(c -> {
                    Category oneByName = categoryService.findOneByName(c.getName());
                    oneByName.addMachine(machineDB);
                    categories.add(oneByName);
                });
            }
            machineDB.setCategories(categories);

            List<FuelConsumptionStandard> fuelConsumptionStandardsDB = fuelConsumptionStandardRepository.findByMachine(machineDB);

            machine.getFuelConsumptionStandards().forEach(nNew -> {

                fuelConsumptionStandardsDB.stream()
                        .filter(nNew::equals).findFirst()
                        .ifPresent(nOld -> {
                            nOld.setValue(nNew.getValue());
                            nOld.setUnit(nNew.getUnit());
                            nOld.setUsedForHeating(nNew.isUsedForHeating());
                            nOld.setRounded(nNew.isRounded());
                            fuelConsumptionStandardRepository.save(nOld);
                        });

                if(!fuelConsumptionStandardsDB.contains(nNew)){
                    nNew.setMachine(machineDB);
                    fuelConsumptionStandardRepository.save(nNew);
                }

            });

        }

    }
    
}
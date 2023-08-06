package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.service.MachineService;

@RequiredArgsConstructor
@RestController
public class MachineRestController {

    private final MachineService machineService;
    private static final Logger logger = LogManager.getLogger(MachineRestController.class);

    @GetMapping("/maszyna")
    public Machine maszynaGet(
            @RequestParam("id") String id,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        logger.info("/maszyna GET id={}", id);
        return machineService.get(id, miesiac);

    }

    @PostMapping(value = "/maszyna")
    public void maszynaPost(
            @RequestBody Machine machine) {

        if(machineService.findById(machine.getId()).isEmpty()){
            logger.info("/maszyna POST id={}", machine.getId());
            machineService.post(machine);
        }
        else{
            throw new BadRequestException("Maszyna " + machine.getId() + " already exists");
        }

    }

    @PutMapping(value = "/maszyna")
    public void maszynaPut(
            @RequestBody Machine machine) {

        if(machineService.findById(machine.getId()).isPresent()){
            logger.info("/maszyna PUT id={}", machine.getId());
            machineService.put(machine);
        }
        else{
            throw new BadRequestException("Maszyna " + machine.getId() + " does not exist");
        }

    }

}
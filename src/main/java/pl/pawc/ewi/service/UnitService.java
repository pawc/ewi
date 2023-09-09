package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Unit;
import pl.pawc.ewi.repository.UnitRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    public List<Unit> findAll(){
        return unitRepository.findAll();
    }

    public void put(Unit unit){
        Unit j = unitRepository.findById(unit.getId())
                .orElse(new Unit());
        j.setName(unit.getName());
        j.setWeightRatio(unit.getWeightRatio());
        unitRepository.save(j);

    }

}
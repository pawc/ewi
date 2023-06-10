package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Jednostka;
import pl.pawc.ewi.repository.JednostkaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JednostkaService {

    private final JednostkaRepository jednostkaRepository;

    public List<Jednostka> findAll(){
        return jednostkaRepository.findAll();
    }

    public void put(Jednostka jednostka){
        Jednostka j = jednostkaRepository.findById(jednostka.getId()).orElse(null);
        if (j == null)  j = new Jednostka();
        j.setNazwa(jednostka.getNazwa());
        j.setWaga(jednostka.getWaga());
        jednostkaRepository.save(j);

    }

}
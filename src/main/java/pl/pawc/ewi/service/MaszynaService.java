package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.repository.MaszynaRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class MaszynaService {
    
    private final MaszynaRepository maszynaRepository;

    public List<Maszyna> findAllActive(){
        Iterable<Maszyna> all = maszynaRepository.findAll();
        Stream<Maszyna> stream = StreamSupport.stream(all.spliterator(), false);
        return stream.filter(Maszyna::isAktywna).collect(Collectors.toList());
    }

    public List<Maszyna> findAllUncategorized(){
        Iterable<Maszyna> all = maszynaRepository.findAll();
        Stream<Maszyna> stream = StreamSupport.stream(all.spliterator(), false);
        return stream.filter(m -> m.getKategorie().isEmpty()).collect(Collectors.toList());
    }
    
}
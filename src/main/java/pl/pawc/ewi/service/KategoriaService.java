package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.repository.KategoriaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KategoriaService {

    private final KategoriaRepository kategoriaRepository;

    public boolean post(Kategoria kategoria){
        boolean exists = kategoriaRepository.findById(kategoria.getNazwa()).isPresent();
        if (!exists) kategoriaRepository.save(kategoria);
        return !exists;
    }

    public void delete(Kategoria kategoria){
        kategoriaRepository
                .findById(kategoria.getNazwa())
                .ifPresent(kategoriaRepository::delete);
    }

    public boolean togglePrzenoszonaNaKolejnyOkres(Kategoria kategoria){
        Optional<Kategoria> byId = kategoriaRepository.findById(kategoria.getNazwa());
        boolean exists = byId.isPresent();
        if(exists){
            Kategoria kat = byId.get();
            kat.setPrzenoszonaNaKolejnyOkres(!kat.isPrzenoszonaNaKolejnyOkres());
            kategoriaRepository.save(kat);
        }
        return exists;
    }

    public List<Kategoria> findAll(){
        return kategoriaRepository.findAll();
    }

}
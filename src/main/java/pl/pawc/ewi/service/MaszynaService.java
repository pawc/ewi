package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class MaszynaService {
    
    private final MaszynaRepository maszynaRepository;
    private final NormaRepository normaRepository;
    private final DokumentRepository dokumentRepository;

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

    public Maszyna get(String id, String miesiac){
        Optional<Maszyna> result = maszynaRepository.findById(id);
        Maszyna maszyna = new Maszyna();
        if(result.isPresent()){

            maszyna = result.get();
            //List<Norma> normy = normaRepository.findByMaszyna(maszyna);

            if(miesiac != null){
                try{
                    int year = Integer.parseInt(miesiac.split("-")[0]);
                    int month = Integer.parseInt(miesiac.split("-")[1]);

                    Double sumaKilometry = dokumentRepository.getSumaKilometry(maszyna.getId(), year, month);
                    sumaKilometry = (sumaKilometry == null) ? 0 : sumaKilometry;
                    maszyna.setSumaKilometry(sumaKilometry);

                    for(Norma norma : maszyna.getNormy()){
                        Double suma = dokumentRepository.getSuma(norma.getId(), year, month);
                        norma.setSuma(suma);
                    }

                }
                catch(NumberFormatException e){
                    // skip
                }
            }
            for(Norma norma : maszyna.getNormy()){
                norma.setMaszyna(null);
            }
            //maszyna.setNormy(maszyna.getNormy());
            return maszyna;
        }
        return maszyna;
    }

    public Maszyna post(Maszyna maszyna){
        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());
        maszyna.getNormy().forEach(n -> n.setMaszyna(maszyna));
        if(!byId.isPresent()) return maszynaRepository.save(maszyna);
        else return null;
    }

    public void put(Maszyna maszyna){
        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(byId.isPresent()) {

            Maszyna maszynaDB = byId.get();
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setAktywna(maszyna.isAktywna());
            maszynaDB.setOpis(maszyna.getOpis());
            maszynaDB.setKategorie(maszyna.getKategorie());

            maszyna.getNormy().forEach(nNew -> {

                maszynaDB.getNormy().stream()
                        .filter(nOld -> nNew.equals(nOld)).findFirst()
                        .ifPresent(nOld -> {
                            nOld.setWartosc(nNew.getWartosc());
                            nOld.setJednostka(nNew.getJednostka());
                            nOld.setCzyOgrzewanie(nNew.isCzyOgrzewanie());
                            normaRepository.save(nOld);
                        });

                if(!maszynaDB.getNormy().contains(nNew)){
                    nNew.setMaszyna(maszynaDB);
                    normaRepository.save(nNew);
                }

            });

        }

    }
    
}
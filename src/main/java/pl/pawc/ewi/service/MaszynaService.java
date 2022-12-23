package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;

import java.math.BigDecimal;
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
    private final ZuzycieService zuzycieService;
    private final DokumentService dokumentService;

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

    @SneakyThrows
    public Maszyna get(String id, String miesiac){
        Optional<Maszyna> result = maszynaRepository.findById(id);
        Maszyna maszyna = new Maszyna();
        if(result.isPresent()){

            maszyna = result.get();
            List<Norma> normy = normaRepository.findByMaszyna(maszyna);
            maszyna.setNormy(normy);

            if(miesiac != null){
                try{
                    int year = Integer.parseInt(miesiac.split("-")[0]);
                    int month = Integer.parseInt(miesiac.split("-")[1]);

                    BigDecimal sumaKilometry = dokumentService.getSumaKilometry(maszyna.getId(), year, month, null);
                    maszyna.setSumaKilometry(sumaKilometry);

                    for(Norma norma : normy){
                        BigDecimal suma = zuzycieService.getSuma(norma.getId(), year, month, null);
                        norma.setSuma(suma);
                    }
                }
                catch(NumberFormatException e){
                    // skip
                }
            }

            return maszyna;
        }
        return maszyna;
    }

    public Maszyna post(Maszyna maszyna){
        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(!byId.isPresent()){
            maszyna.getNormy().forEach(n -> {
                n.setMaszyna(maszyna);
                normaRepository.save(n);
            });
            return maszyna;
        }
        else{
            return null;
        }
    }

    public void put(Maszyna maszyna){
        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(byId.isPresent()) {

            Maszyna maszynaDB = byId.get();
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setAktywna(maszyna.isAktywna());
            maszynaDB.setOpis(maszyna.getOpis());
            maszynaDB.setKategorie(maszyna.getKategorie());

            List<Norma> normyDB = normaRepository.findByMaszyna(maszynaDB);

            maszyna.getNormy().forEach(nNew -> {

                normyDB.stream()
                        .filter(nNew::equals).findFirst()
                        .ifPresent(nOld -> {
                            nOld.setWartosc(nNew.getWartosc());
                            nOld.setJednostka(nNew.getJednostka());
                            nOld.setCzyOgrzewanie(nNew.isCzyOgrzewanie());
                            nOld.setCzyZaokr1setna(nNew.isCzyZaokr1setna());
                            normaRepository.save(nOld);
                        });

                if(!normyDB.contains(nNew)){
                    nNew.setMaszyna(maszynaDB);
                    normaRepository.save(nNew);
                }

            });

        }

    }
    
}
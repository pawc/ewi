package pl.pawc.ewi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.sql.Date;

@Component
public class InitialDataLoader implements ApplicationRunner {

    @Autowired
    MaszynaRepository maszynaRepository;

    @Autowired
    DokumentRepository dokumentRepository;

    @Autowired
    NormaRepository normaRepository;

    @Autowired
    ZuzycieRepository zuzycieRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*Maszyna maszyna = new Maszyna();
        maszyna.setId(123);
        maszyna.setNazwa("Wózek");
        maszyna.setOpis("Przykładowy opis wózka");
        maszynaRepository.save(maszyna);

        Norma norma = new Norma();
        norma.setJednostka("litrów oleju/motogodzinę");
        norma.setWartosc(11.2);
        norma.setMaszyna(maszyna);
        normaRepository.save(norma);

        norma = new Norma();
        norma.setJednostka("litrów etyliny/motogodzinę");
        norma.setWartosc(5.25);
        norma.setMaszyna(maszyna);
        normaRepository.save(norma);*/

    }

}
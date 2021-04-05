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

        Maszyna maszyna = new Maszyna();
        maszyna.setId("W123");
        maszyna.setNazwa("Wózek");
        maszyna.setOpis("Przykładowy opis wózka");
        maszynaRepository.save(maszyna);

        Norma norma = new Norma();
        norma.setJednostka("litrów oleju/motogodzinę");
        norma.setWartosc(7.5);
        norma.setMaszyna(maszyna);
        normaRepository.save(norma);

        Zuzycie zuzycie1 = new Zuzycie();
        zuzycie1.setNorma(norma);
        zuzycie1.setWartosc(3);

        Zuzycie zuzycie3 = new Zuzycie();
        zuzycie3.setNorma(norma);
        zuzycie3.setWartosc(4);

        norma = new Norma();
        norma.setJednostka("litrów opału/godzinę");
        norma.setWartosc(2.1);
        norma.setMaszyna(maszyna);
        normaRepository.save(norma);

        Zuzycie zuzycie2 = new Zuzycie();
        zuzycie2.setNorma(norma);
        zuzycie2.setWartosc(3);

        Zuzycie zuzycie4 = new Zuzycie();
        zuzycie4.setNorma(norma);
        zuzycie4.setWartosc(4);

        Dokument dokument = new Dokument();
        dokument.setData(new Date(System.currentTimeMillis()));
        dokument.setNumer("DOK-1/2021");
        dokument.setMaszyna(maszyna);
        dokumentRepository.save(dokument);

        zuzycie1.setDokument(dokument);
        zuzycie2.setDokument(dokument);
        zuzycieRepository.save(zuzycie1);
        zuzycieRepository.save(zuzycie2);

        dokument = new Dokument();
        dokument.setData(new Date(System.currentTimeMillis()));
        dokument.setNumer("DOK-2/2021");
        dokument.setMaszyna(maszyna);
        dokumentRepository.save(dokument);

        zuzycie3.setDokument(dokument);
        zuzycie4.setDokument(dokument);
        zuzycieRepository.save(zuzycie3);
        zuzycieRepository.save(zuzycie4);

    }

}
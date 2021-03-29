package pl.pawc.ewi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.Paliwo;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import java.sql.Date;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    MaszynaRepository maszynaRepository;

    @Autowired
    DokumentRepository dokumentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Maszyna maszyna = new Maszyna();
        maszyna.setId(123);
        maszyna.setPaliwo(Paliwo.OLEJ);
        maszyna.setOpis("dodatkowy opis");
        maszyna.setNazwa("Snopowiązałka");

        maszynaRepository.save(maszyna);

        Dokument dokument = new Dokument();
        dokument.setNumer("ABC123");
        dokument.setIlosc(12.4);
        dokument.setData(Date.valueOf("2021-03-12"));
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        dokument = new Dokument();
        dokument.setNumer("DEF456");
        dokument.setIlosc(6.9);
        dokument.setData(Date.valueOf("2021-01-23"));
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        maszyna = new Maszyna();
        maszyna.setId(987);
        maszyna.setPaliwo(Paliwo.BENZYNA);
        maszyna.setOpis("Lorem ipsum dolor sit amet");
        maszyna.setNazwa("VW Caddy");

        maszynaRepository.save(maszyna);

        dokument = new Dokument();
        dokument.setNumer("GHI423/12");
        dokument.setIlosc(11.2);
        dokument.setData(Date.valueOf("2021-02-18"));
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        dokument = new Dokument();
        dokument.setNumer("KJHS/2021");
        dokument.setIlosc(7.11);
        dokument.setData(Date.valueOf("2021-01-25"));
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        dokument = new Dokument();
        dokument.setNumer("VBC/2/21");
        dokument.setIlosc(8.2);
        dokument.setData(Date.valueOf("2021-02-01"));
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

    }

}
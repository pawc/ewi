package pl.pawc.ewi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Calendar;

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

    @Value( "${testDataLoad}" )
    private String testDataLoadString;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Boolean testDataLoad = Boolean.valueOf(testDataLoadString);

        if(testDataLoad){

            Calendar cal = Calendar.getInstance();
            Date today = new Date(System.currentTimeMillis());
            cal.setTime(today);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            String docNumberSuffix = "/" + String.valueOf(month) + "/" + String.valueOf(year);

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
            zuzycie1.setZatankowano(0);

            Zuzycie zuzycie3 = new Zuzycie();
            zuzycie3.setNorma(norma);
            zuzycie3.setWartosc(4);
            zuzycie3.setZatankowano(0);

            norma = new Norma();
            norma.setJednostka("litrów opału/godzinę");
            norma.setWartosc(2.1);
            norma.setMaszyna(maszyna);
            normaRepository.save(norma);

            Zuzycie zuzycie2 = new Zuzycie();
            zuzycie2.setNorma(norma);
            zuzycie2.setWartosc(3);
            zuzycie2.setZatankowano(0);

            Zuzycie zuzycie4 = new Zuzycie();
            zuzycie4.setNorma(norma);
            zuzycie4.setWartosc(4);
            zuzycie4.setZatankowano(0);

            Dokument dokument = new Dokument();
            dokument.setData(today);

            dokument.setNumer("1" + docNumberSuffix);
            dokument.setMaszyna(maszyna);
            dokumentRepository.save(dokument);

            zuzycie1.setDokument(dokument);
            zuzycie2.setDokument(dokument);
            zuzycieRepository.save(zuzycie1);
            zuzycieRepository.save(zuzycie2);

            dokument = new Dokument();
            dokument.setData(today);
            dokument.setNumer("2" + docNumberSuffix);
            dokument.setMaszyna(maszyna);
            dokumentRepository.save(dokument);

            zuzycie3.setDokument(dokument);
            zuzycie4.setDokument(dokument);
            zuzycieRepository.save(zuzycie3);
            zuzycieRepository.save(zuzycie4);

        }

    }

}
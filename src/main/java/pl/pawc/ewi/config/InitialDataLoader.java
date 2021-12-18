package pl.pawc.ewi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.UserRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.sql.Date;
import java.util.Calendar;

@RequiredArgsConstructor
@Component
public class InitialDataLoader implements ApplicationRunner {

    private final MaszynaRepository maszynaRepository;
    private final DokumentRepository dokumentRepository;
    private final NormaRepository normaRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StanRepository stanRepository;

    @Value("${testDataLoad}")
    private String testDataLoadString;

    @Value("${myUser}")
    private String myUser;

    @Value("${myPassword}")
    private String myPassword;

    @Override
    public void run(ApplicationArguments args){

        testData();
        myUser();

    }

    private void myUser() {
        User user = new User();
        if(myUser.length() > 4 && myPassword.length() > 4 && !userRepository.findById(myUser).isPresent()){
            user.setLogin(myUser);
            user.setPassword(passwordEncoder.encode(myPassword));
            userRepository.save(user);
        }
        else {
            if(!userRepository.findById(myUser).isPresent()){
                user.setLogin("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                userRepository.save(user);
            }
        }
    }

    private void testData() {
        boolean testDataLoad = Boolean.parseBoolean(testDataLoadString);
        if(testDataLoad){

            Calendar cal = Calendar.getInstance();
            Date today = new Date(System.currentTimeMillis());
            cal.setTime(today);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            String docNumberSuffix = "/" + month + "/" + year;

            Maszyna maszyna = new Maszyna();
            maszyna.setId("W123");
            maszyna.setNazwa("Woz");
            maszyna.setOpis("Opis 1");
            maszynaRepository.save(maszyna);

            Norma norma = new Norma();
            norma.setJednostka("ON/H");
            norma.setWartosc(7.5);
            norma.setMaszyna(maszyna);
            normaRepository.save(norma);

            Stan stan = new Stan();
            stan.setNorma(norma);
            stan.setMiesiac(month);
            stan.setRok(year);
            stan.setWartosc(200);
            stanRepository.save(stan);

            Zuzycie zuzycie1 = new Zuzycie();
            zuzycie1.setNorma(norma);
            zuzycie1.setWartosc(3);
            zuzycie1.setZatankowano(0);

            Zuzycie zuzycie3 = new Zuzycie();
            zuzycie3.setNorma(norma);
            zuzycie3.setWartosc(4);
            zuzycie3.setZatankowano(0);

            norma = new Norma();
            norma.setJednostka("A/H");
            norma.setWartosc(2.1);
            norma.setMaszyna(maszyna);
            normaRepository.save(norma);

            stan = new Stan();
            stan.setNorma(norma);
            stan.setMiesiac(month);
            stan.setRok(year);
            stan.setWartosc(20);
            stanRepository.save(stan);

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
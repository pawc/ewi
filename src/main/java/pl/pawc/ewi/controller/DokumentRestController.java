package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class DokumentRestController {

    private static final Logger logger = LogManager.getLogger(DokumentRestController.class);
    private final DokumentRepository dokumentRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final MaszynaRepository maszynaRepository;
    private final NormaRepository normaRepository;
    private final StanRepository stanRepository;
    private final KilometryRepository kilometryRepository;

    @RequestMapping("/dokument")
    public Dokument dokumentGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("numer") String numer,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        Optional<Dokument> result = dokumentRepository.findById(numer);
        Dokument dokument;
        if(result.isPresent()){
            dokument = result.get();
            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokument(dokument);
            for(Zuzycie zuzycie : zuzycieList){
                if(miesiac != null){
                    int year;
                    int month;

                    try{
                        year = Integer.parseInt(miesiac.split("-")[0]);
                        month = Integer.parseInt(miesiac.split("-")[1]);
                        Double suma = dokumentRepository.getSuma(zuzycie.getNorma().getId(), year, month);
                        Double sumaBefore = dokumentRepository.getSumBeforeDate(
                                zuzycie.getNorma().getId(), year, month, dokument.getData(), dokument.getNumer());

                        double stan = 0D;
                        List<Stan> by = stanRepository.findBy(zuzycie.getNorma(), year, month);
                        if(!by.isEmpty()) stan = by.get(0).getWartosc();

                        zuzycie.getNorma().setSuma(suma);
                        zuzycie.getNorma().setSumaBefore(sumaBefore);
                        zuzycie.getNorma().setStan(stan);
                    }
                    catch(NumberFormatException e){
                        // skip
                    }
                }

                zuzycie.setDokument(null);
                zuzycie.getNorma().setMaszyna(null);
            }
            dokument.setZuzycie(zuzycieList);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dokument.getData());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            Double kilometryBefore = dokumentRepository.getSumaKilometryBeforeDate(dokument.getMaszyna().getId(),
                    year, month, dokument.getData(), dokument.getNumer());
            if(kilometryBefore == null || kilometryBefore == 0D){
                kilometryBefore = 0D;
                List<Kilometry> by1 = kilometryRepository.findBy(dokument.getMaszyna(), year, month);
                if(!by1.isEmpty()) kilometryBefore = by1.get(0).getWartosc();
            }
            dokument.setKilometryBefore(kilometryBefore);

            logger.info("["+request.getRemoteAddr()+"] - /dokument GET numer="+numer);
        }
        else{
            dokument = new Dokument();
            logger.warn("["+request.getRemoteAddr()+"] - /dokument GET numer="+numer + ". Nie odnaleziono");
        }
        return dokument;

    }

    @RequestMapping("/dokumentyGet")
    public List<Dokument> dokumentyGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        return dokumentRepository.getDokumenty(rok, miesiac);

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.POST)
    public void dokumentPost(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Zuzycie> zuzycia = dokument.getZuzycie();
        for(Zuzycie zuzycie : zuzycia){
            normaRepository.findById(zuzycie.getNorma().getId()).ifPresent(zuzycie::setNorma);
        }

        Maszyna maszyna = maszynaRepository.findById(dokument.getMaszyna().getId()).orElse(null);
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        for(Zuzycie zuzycie : zuzycia){
            zuzycie.setDokument(dokument);
            zuzycieRepository.save(zuzycie);
        }

        logger.info("["+request.getRemoteAddr()+"] - /dokument POST numer="+dokument.getNumer());

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.PUT)
    public void dokumentPut(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Dokument> byId = dokumentRepository.findById(dokument.getNumer());

        if(byId.isPresent()){
            Dokument dokumentDB = byId.get();

            dokumentDB.setData(dokument.getData());
            dokumentDB.setKilometry(dokument.getKilometry());
            dokumentDB.setKilometryPrzyczepa(dokument.getKilometryPrzyczepa());
            dokumentRepository.save(dokumentDB);

            for(Zuzycie zuzycie : dokument.getZuzycie()) {
                Zuzycie zuzycieDB = zuzycieRepository.findById(zuzycie.getId()).orElse(null);
                if(zuzycieDB == null) continue;
                if (zuzycieDB.getWartosc() != zuzycie.getWartosc() ||
                    zuzycieDB.getZatankowano() != zuzycie.getZatankowano() ||
                    zuzycieDB.getOgrzewanie() != zuzycie.getOgrzewanie()) {

                    zuzycieDB.setWartosc(zuzycie.getWartosc());
                    zuzycieDB.setZatankowano(zuzycie.getZatankowano());
                    zuzycieDB.setOgrzewanie(zuzycie.getOgrzewanie());
                    zuzycieRepository.save(zuzycieDB);
                }
            }

            logger.info("["+request.getRemoteAddr()+"] - /dokument PUT numer="+dokument.getNumer());

        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("["+request.getRemoteAddr()+"] - /dokument PUT - BAD REQUEST");
        }

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.DELETE)
    public void dokumentDelete(
            @RequestParam("numer") String numer,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Dokument> byId = dokumentRepository.findById(numer);

        if(byId.isPresent()) {
            Dokument dokumentDB = byId.get();

            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokument(dokumentDB);
            zuzycieRepository.deleteAll(zuzycieList);
            dokumentRepository.delete(dokumentDB);

        }

        logger.info("["+request.getRemoteAddr()+"] - /dokument DELETE numer="+numer);

    }

}
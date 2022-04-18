package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Kategoria;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KategoriaRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.service.MaszynaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class MaszynaRestController {

    private final MaszynaRepository maszynaRepository;
    private final NormaRepository normaRepository;
    private final DokumentRepository dokumentRepository;
    private final KategoriaRepository kategoriaRepository;
    private final MaszynaService maszynaService;
    private static final Logger logger = LogManager.getLogger(MaszynaRestController.class);

    @RequestMapping("/maszyna")
    public Maszyna maszynaGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") String id,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        Optional<Maszyna> result = maszynaRepository.findById(id);
        Maszyna maszyna;
        if(result.isPresent()){

            maszyna = result.get();
            List<Norma> normy = normaRepository.findByMaszyna(maszyna);

            if(miesiac != null){
                try{
                    int year = Integer.parseInt(miesiac.split("-")[0]);
                    int month = Integer.parseInt(miesiac.split("-")[1]);

                    Double sumaKilometry = dokumentRepository.getSumaKilometry(maszyna.getId(), year, month);
                    sumaKilometry = (sumaKilometry == null) ? 0 : sumaKilometry;
                    maszyna.setSumaKilometry(sumaKilometry);

                    for(Norma norma : normy){
                        Double suma = dokumentRepository.getSuma(norma.getId(), year, month);
                        norma.setSuma(suma);
                        norma.setMaszyna(null);
                    }

                }
                catch(NumberFormatException e){
                    // skip
                }
            }
            else{
                for(Norma norma : normy){
                    norma.setMaszyna(null);
                }
            }
            maszyna.setNormy(normy);
            logger.info("[{}] /maszyna GET id={}", ip, id);
            return maszyna;
        }
        else{
            logger.warn("[{}] /maszyna GET id={} - nie odnaleziono", ip, id);
            maszyna = new Maszyna();
        }
        return maszyna;

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.POST)
    public void maszynaPost(
            @RequestBody Maszyna maszyna,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();

        if(maszynaService.post(maszyna) != null){
            logger.info("[{}] /maszyna POST id={}", ip, maszyna.getId());
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("[{}] /maszyna POST id={} BAD REQUEST", ip, maszyna.getId());
        }

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.PUT)
    public void maszynaPut(
            @RequestBody Maszyna maszyna,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(byId.isPresent()){
            Maszyna maszynaDB = byId.get();
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setOpis(maszyna.getOpis());
            maszynaDB.setAktywna(maszyna.isAktywna());

            List<Norma> normy = normaRepository.findByMaszyna(maszyna);
            for(Norma normaNew : maszyna.getNormy()){
                boolean test = false;
                for(Norma normaOld : normy){
                    if(normaOld.getJednostka().equals(normaNew.getJednostka())){
                        test = true;
                        break;
                    }
                }
                if(!test){
                    normaNew.setMaszyna(maszynaDB);
                    normaRepository.save(normaNew);
                }
            }

            maszynaDB.getKategorie().clear();
            for(Kategoria kategoria : maszyna.getKategorie()){
                Kategoria kat = kategoriaRepository.findById(kategoria.getNazwa()).orElse(null);
                maszynaDB.getKategorie().add(kat);
            }

            maszynaRepository.save(maszynaDB);
            logger.info("[{}] /maszyna PUT id={}", ip, maszyna.getId());
            logger.info("["+request.getHeader("X-Real-IP")+"] - /maszyna PUT id="+maszyna.getId());
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("[{}] /maszyna PUT - BAD REQUEST", ip);
        }

    }

}
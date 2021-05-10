package pl.pawc.ewi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;
import pl.pawc.ewi.repository.NormaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class MaszynaRestController {

    @Autowired
    MaszynaRepository maszynaRepository;

    @Autowired
    NormaRepository normaRepository;

    @Autowired
    DokumentRepository dokumentRepository;

    private static final Logger logger = Logger.getLogger(MaszynaRestController.class);

    @RequestMapping("/maszyna")
    public Maszyna maszynaGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") String id,
            @RequestParam(name = "miesiac", required = false) String miesiac){

        Optional<Maszyna> result = maszynaRepository.findById(id);
        Maszyna maszyna = null;
        if(result.isPresent()){
            maszyna = result.get();
            List<Norma> normy = normaRepository.findByMaszynaId(id);
            for(Norma norma : normy){
                norma.setMaszyna(null);
                int year;
                int month;

                if(miesiac != null){
                    try{
                        year = Integer.parseInt(miesiac.split("-")[0]);
                        month = Integer.parseInt(miesiac.split("-")[1]);
                        Double suma = dokumentRepository.getSuma(norma.getId(), year, month);
                        norma.setSuma(suma);
                    }
                    catch(NumberFormatException e){
                        // skip
                    }
                }

            }
            maszyna.setNormy(normy);
            logger.info("["+request.getRemoteAddr()+"] - /maszyna GET id="+id );
            return maszyna;
        }
        else{
            logger.info("["+request.getRemoteAddr()+"] - /maszyna GET id="+id + ". Nie odnaleziono");
            maszyna = new Maszyna();
        }
        return maszyna;

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.POST)
    public void maszynaPost(
            @RequestBody Maszyna maszyna,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(!byId.isPresent()){
            Maszyna maszynaNew = new Maszyna();
            maszynaNew.setId(maszyna.getId());
            maszynaNew.setNazwa(maszyna.getNazwa());
            maszynaNew.setOpis(maszyna.getOpis());
            maszynaRepository.save(maszynaNew);

            Norma normaNew;
            for(Norma norma : maszyna.getNormy()){
                normaNew = new Norma();
                normaNew.setJednostka(norma.getJednostka());
                normaNew.setWartosc(norma.getWartosc());
                normaNew.setMaszyna(maszynaNew);
                normaNew.setCzyOgrzewanie(norma.isCzyOgrzewanie());
                normaRepository.save(normaNew);
            }
            logger.info("["+request.getRemoteAddr()+"] - /maszyna POST id="+maszyna.getId());
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("["+request.getRemoteAddr()+"] - /maszyna POST - BAD REQUEST");
        }

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.PUT)
    public void maszynaPut(
            @RequestBody Maszyna maszyna,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(byId.isPresent()){
            Maszyna maszynaDB = byId.get();
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setOpis(maszyna.getOpis());

            List<Norma> normy = normaRepository.findByMaszynaId(maszyna.getId());
            for(Norma normaNew : maszyna.getNormy()){
                boolean test = false;
                for(Norma normaOld : normy){
                    if(normaOld.getJednostka().equals(normaNew.getJednostka())) test = true;
                }
                if(!test){
                    normaNew.setMaszyna(maszynaDB);
                    normaRepository.save(normaNew);
                }
            }
            maszynaRepository.save(maszynaDB);
            logger.info("["+request.getRemoteAddr()+"] - /maszyna PUT id="+maszyna.getId());
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("["+request.getRemoteAddr()+"] - /maszyna PUT - BAD REQUEST");
        }

    }

}
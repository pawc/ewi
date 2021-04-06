package pl.pawc.ewi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class EwiRestController {

    @Autowired
    DokumentRepository dokumentRepository;

    @Autowired
    MaszynaRepository maszynaRepository;

    @Autowired
    NormaRepository normaRepository;

    @Autowired
    ZuzycieRepository zuzycieRepository;

    @Autowired
    RaportRepository raportRepository;

    @RequestMapping("/maszyna")
    public Maszyna maszynaGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") String id){

        Optional<Maszyna> result = maszynaRepository.findById(id);
        Maszyna maszyna = null;
        if(result.isPresent()){
            maszyna = result.get();
            List<Norma> normy = normaRepository.findByMaszynaId(id);
            for(Norma norma : normy){
                norma.setMaszyna(null);
            }
            maszyna.setNormy(normy);
            return maszyna;
        }
        else{
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
                normaRepository.save(normaNew);
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @RequestMapping("/dokument")
    public Dokument dokumentGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("numer") String numer){

        Optional<Dokument> result = dokumentRepository.findById(numer);
        Dokument dokument = null;
        if(result.isPresent()){
            dokument = result.get();
            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokumentId(dokument.getNumer());
            for(Zuzycie zuzycie : zuzycieList){
                zuzycie.setDokument(null);
                zuzycie.getNorma().setMaszyna(null);
            }
            dokument.setZuzycie(zuzycieList);
        }
        else{
            dokument = new Dokument();
        }
        return dokument;

    }

    @RequestMapping(value = "/dokument", method = RequestMethod.POST)
    public void dokumentPost(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        List<Zuzycie> zuzycia = dokument.getZuzycie();
        for(Zuzycie zuzycie : zuzycia){
            Norma norma = normaRepository.findById(zuzycie.getNorma().getId()).get();
            zuzycie.setNorma(norma);
        }

        Maszyna maszyna = maszynaRepository.findById(dokument.getMaszyna().getId()).get();
        dokument.setMaszyna(maszyna);

        dokumentRepository.save(dokument);

        for(Zuzycie zuzycie : zuzycia){
            zuzycie.setDokument(dokument);
            zuzycieRepository.save(zuzycie);
        }

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
            dokumentRepository.save(dokumentDB);

            for(Zuzycie zuzycie : dokument.getZuzycie()) {
                Zuzycie zuzycieDB = zuzycieRepository.findById(zuzycie.getId()).get();
                if (zuzycieDB.getWartosc() != zuzycie.getWartosc()) {
                    zuzycieDB.setWartosc(zuzycie.getWartosc());
                    zuzycieRepository.save(zuzycieDB);
                }
            }

        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

            List<Zuzycie> zuzycieList = zuzycieRepository.findByDokumentId(dokumentDB.getNumer());
            zuzycieRepository.deleteAll(zuzycieList);
            dokumentRepository.delete(dokumentDB);

        }

    }

    @RequestMapping(value = "/raport", method = RequestMethod.GET)
    public List<Raport> raport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        List<Raport> raport = raportRepository.getRaport(rok, miesiac);

        return raport;

    }

}
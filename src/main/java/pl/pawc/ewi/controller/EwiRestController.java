package pl.pawc.ewi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class EwiRestController {

    @Autowired
    DokumentRepository dokumentRepository;

    @Autowired
    MaszynaRepository maszynaRepository;

    @RequestMapping("/maszyna/{id}")
    public Maszyna maszynaGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable Integer id){

        Maszyna maszyna = maszynaRepository.findById(id).get();

        return maszyna;

    }

    @RequestMapping(value = "/maszyna", method = RequestMethod.POST)
    public void maszynaPost(
            @RequestBody Maszyna maszyna,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Maszyna> byId = maszynaRepository.findById(maszyna.getId());

        if(byId.isPresent()){
            Maszyna maszynaDB = byId.get();
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setPaliwo(maszyna.getPaliwo());
            maszynaDB.setOpis(maszyna.getOpis());
            maszynaRepository.save(maszynaDB);
        }
        else{
            maszynaRepository.save(maszyna);
        }

    }

    @RequestMapping(value = "/usunMaszyne", method = RequestMethod.POST)
    public void usunMaszyne(
            @RequestParam Integer id,
            HttpServletRequest request,
            HttpServletResponse response) {

        maszynaRepository.deleteById(id);

    }


    @RequestMapping(value = "/dokument", method = RequestMethod.POST)
    public void dokumentPost(
            @RequestBody Dokument dokument,
            HttpServletRequest request,
            HttpServletResponse response) {

        Optional<Maszyna> byId = maszynaRepository.findById(dokument.getMaszyna().getId());

        if(byId.isPresent()){
            Maszyna maszynaDB = byId.get();
            dokument.setMaszyna(maszynaDB);
            dokumentRepository.save(dokument);
            maszynaDB.getDokumenty().add(dokument);
            maszynaRepository.save(maszynaDB);
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

            Maszyna maszynaOld = maszynaRepository.findById(dokumentDB.getMaszyna().getId()).get();
            maszynaOld.getDokumenty().remove(dokumentDB);
            maszynaRepository.save(maszynaOld);

            Maszyna maszynaNew = maszynaRepository.findById(dokument.getMaszyna().getId()).get();

            dokumentDB.setMaszyna(maszynaNew);
            dokumentDB.setData(dokument.getData());
            dokumentDB.setIlosc(dokument.getIlosc());
            dokumentRepository.save(dokumentDB);

            maszynaNew.getDokumenty().add(dokumentDB);
            maszynaRepository.save(maszynaNew);

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

        Dokument dokument = dokumentRepository.findById(numer).get();
        Maszyna maszyna = dokument.getMaszyna();
        Maszyna m = new Maszyna();
        m.setId(maszyna.getId());
        m.setPaliwo(maszyna.getPaliwo());
        m.setNazwa(maszyna.getNazwa());
        dokument.setMaszyna(m);

        return dokument;

    }

}
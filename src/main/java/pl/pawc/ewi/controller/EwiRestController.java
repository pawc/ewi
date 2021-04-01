package pl.pawc.ewi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        Maszyna maszynaDB = maszynaRepository.findById(maszyna.getId()).get();

        if(maszynaDB != null){
            maszynaDB.setNazwa(maszyna.getNazwa());
            maszynaDB.setPaliwo(maszyna.getPaliwo());
            maszynaDB.setOpis(maszyna.getOpis());
            maszynaRepository.save(maszynaDB);
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}
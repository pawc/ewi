package pl.pawc.ewi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.model.BadRequestException;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.service.DocumentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DocumentRestController {

    private static final Logger logger = LogManager.getLogger(DocumentRestController.class);
    private final DocumentService documentService;

    @RequestMapping("/dokument")
    public Document dokumentGet(
            @RequestParam("numer") String numer) {

        logger.info("/dokument GET numer={} ", numer);
        Document document = documentService.get(numer);
        return document == null ? new Document() : document;

    }

    @RequestMapping("/dokumentyGet")
    public List<Document> dokumentyGet(
            @RequestParam("rok") int rok,
            @RequestParam("miesiac") int miesiac){

        logger.info("/dokumentyGet {}-{} ", rok, miesiac);
        List<Document> dokumenty = documentService.getDokumenty(rok, miesiac);
        dokumenty.forEach(d -> d.setFuelConsumption(null));
        return dokumenty;

    }

    @PostMapping("/dokument")
    public void dokumentPost(
            @RequestBody Document document) {

        logger.info("/dokument POST numer={}", document.getNumber());
        if(StringUtils.isEmpty(document.getNumber()) ||
                document.getMachine().getId() == null) throw new BadRequestException("No document details");

        if(documentService.findById(document.getNumber()).isEmpty()) documentService.post(document);
        else throw new BadRequestException("document" + document.getNumber() + " already exists");

    }

    @PutMapping("/dokument")
    public void dokumentPut(
            @RequestBody Document document) {

        try {
            documentService.put(document);
            logger.info("/dokument PUT numer={}", document.getNumber());
        }
        catch (DocumentNotFoundException e) {
            logger.warn("/dokument PUT - DocumentNotFoundException");
            throw new BadRequestException();
        }

    }

    @DeleteMapping("/dokument")
    public void dokumentDelete(
            @RequestParam("numer") String numer) {

        logger.info("/dokument DELETE numer={}", numer);
        documentService.delete(numer);


    }

}
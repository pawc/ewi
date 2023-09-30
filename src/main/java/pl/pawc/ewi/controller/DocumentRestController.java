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

    @RequestMapping("/document")
    public Document document(
            @RequestParam("number") String number) {

        logger.debug("/document GET number={} ", number);
        Document document = documentService.get(number);
        return document == null ? new Document() : document;

    }

    @RequestMapping("/documents")
    public List<Document> documents(
            @RequestParam("year") int year,
            @RequestParam("month") int month){

        logger.debug("/documents GET {}-{} ", year, month);
        List<Document> documents = documentService.getDocuments(year, month);
        documents.forEach(d -> d.setFuelConsumption(null));
        return documents;

    }

    @PostMapping("/document")
    public void documentPost(
            @RequestBody Document document) {

        logger.info("/document POST number={}", document.getNumber());
        if(StringUtils.isEmpty(document.getNumber()) ||
                document.getMachine().getId() == null) throw new BadRequestException("No document details");

        if(documentService.findById(document.getNumber()).isEmpty()) documentService.post(document);
        else throw new BadRequestException("document" + document.getNumber() + " already exists");

    }

    @PutMapping("/document")
    public void documentPut(
            @RequestBody Document document) {

        try {
            documentService.put(document);
            logger.info("/document PUT number={}", document.getNumber());
        }
        catch (DocumentNotFoundException e) {
            logger.warn("/document PUT - DocumentNotFoundException");
            throw new BadRequestException();
        }

    }

    @DeleteMapping("/document")
    public void documentDelete(
            @RequestParam("number") String number) {

        logger.info("/document DELETE number={}", number);
        documentService.delete(number);

    }

}
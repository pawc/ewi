package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.NormaNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;
    private final KilometryRepository kilometryRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final StanRepository stanRepository;
    private final ZuzycieService zuzycieService;

    public List<Dokument> getDokumenty(int year, int month){
        return dokumentRepository.getDokumenty(year, month);
    }

    public BigDecimal getSumaKilometry(String maszynaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException {

        final Dokument dokument;
        Calendar calD = Calendar.getInstance();
        if(excludedDocNumber != null){
            dokument = dokumentRepository.findById(excludedDocNumber).orElseThrow(() -> new DocumentNotFoundException((excludedDocNumber)));
            calD.setTime(dokument.getData());
        }
        else{
            dokument = null;
        }

        Maszyna maszyna = new Maszyna();
        maszyna.setId(maszynaId);

        Calendar cal = Calendar.getInstance();

        Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(maszyna, year, month);
        BigDecimal stan = kilometry != null ? kilometry.getWartosc() : BigDecimal.ZERO;

        BigDecimal reduce = dokumentRepository.findByMaszyna(maszyna).stream().filter(d -> {
            cal.setTime(d.getData());
            return (cal.get(Calendar.MONTH) + 1) == month
                    && cal.get(Calendar.YEAR) == year
                    && !d.getNumer().equals(excludedDocNumber)
                    && (dokument == null || calD.get(Calendar.DAY_OF_MONTH) >= cal.get(Calendar.DAY_OF_MONTH));
        }).map(Dokument::getKilometry).reduce(BigDecimal.ZERO, BigDecimal::add);

        return stan.add(reduce);

    }

    public Dokument get(String numer) {

        Optional<Dokument> result = dokumentRepository.findById(numer);
        Dokument dokument = null;

        if(result.isPresent()){
            dokument = result.get();

            Calendar cal = Calendar.getInstance();
            cal.setTime(dokument.getData());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            List<Zuzycie> zuzycia = zuzycieRepository.findByDokument(dokument);
            dokument.setZuzycie(zuzycia);

            for(Zuzycie zuzycie : zuzycia){

                BigDecimal suma = null;
                BigDecimal sumaBefore = null;
                zuzycie.setDokument(dokument);
                try {
                    suma = zuzycieService.getSuma(zuzycie.getNorma().getId(), year, month, null);
                    sumaBefore = zuzycieService.getSuma(zuzycie.getNorma().getId(), year, month, dokument.getNumer());

                } catch (DocumentNotFoundException | NormaNotFoundException e) {
                    e.printStackTrace();
                }

                Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(zuzycie.getNorma(), year, month);
                BigDecimal stanD = stan == null ? BigDecimal.ZERO : stan.getWartosc();

                zuzycie.getNorma().setSuma(suma);
                zuzycie.getNorma().setSumaBefore(sumaBefore);
                zuzycie.getNorma().setStan(stanD);

                zuzycie.setDokument(null);
                zuzycie.getNorma().setMaszyna(null);

            }

            BigDecimal kilometryBefore = null;
            try {
                kilometryBefore = getSumaKilometry(dokument.getMaszyna().getId(), year, month, dokument.getNumer());
            } catch (DocumentNotFoundException e) {
                e.printStackTrace();
            }
            dokument.setKilometryBefore(kilometryBefore);

        }
        return dokument;

    }

    public void post(Dokument dokument){
        if(dokument.getKilometry() == null) dokument.setKilometry(BigDecimal.ZERO);
        if(dokument.getKilometryPrzyczepa() == null) dokument.setKilometryPrzyczepa(BigDecimal.ZERO);
        for(Zuzycie zuzycie : dokument.getZuzycie()){
            if(zuzycie.getWartosc() == null) zuzycie.setWartosc(BigDecimal.ZERO);
            if(zuzycie.getZatankowano() == null) zuzycie.setZatankowano(BigDecimal.ZERO);
            if(zuzycie.getOgrzewanie() == null) zuzycie.setOgrzewanie(BigDecimal.ZERO);
            zuzycie.setDokument(dokument);
            zuzycieRepository.save(zuzycie);
        }
    }

    public void put(Dokument dokument) throws DocumentNotFoundException {
        Optional<Dokument> byId = dokumentRepository.findById(dokument.getNumer());
        if(dokument.getKilometry() == null) dokument.setKilometry(BigDecimal.ZERO);
        if(dokument.getKilometryPrzyczepa() == null) dokument.setKilometryPrzyczepa(BigDecimal.ZERO);

        if(byId.isPresent()){
            Dokument dokumentDB = byId.get();

            dokumentDB.setData(dokument.getData());
            dokumentDB.setKilometry(dokument.getKilometry());
            dokumentDB.setKilometryPrzyczepa(dokument.getKilometryPrzyczepa());
            dokumentRepository.save(dokumentDB);

            for(Zuzycie zuzycie : dokument.getZuzycie()) {
                Zuzycie zuzycieDB = zuzycieRepository.findById(zuzycie.getId()).orElse(null);
                if(zuzycieDB == null) continue;
                if(zuzycie.getWartosc() == null) zuzycie.setWartosc(BigDecimal.ZERO);
                if(zuzycie.getZatankowano() == null) zuzycie.setZatankowano(BigDecimal.ZERO);
                if(zuzycie.getOgrzewanie() == null) zuzycie.setOgrzewanie(BigDecimal.ZERO);
                zuzycieDB.setWartosc(zuzycie.getWartosc());
                zuzycieDB.setZatankowano(zuzycie.getZatankowano());
                zuzycieDB.setOgrzewanie(zuzycie.getOgrzewanie());

                zuzycieRepository.save(zuzycieDB);
            }
        }
        else{
            throw new DocumentNotFoundException(dokument.getNumer());
        }
    }

    public void delete(String numer) {

        dokumentRepository.findById(numer).ifPresent(dok -> {
            zuzycieRepository.findByDokument(dok).forEach(zuzycieRepository::delete);
            dokumentRepository.delete(dok);
        });

    }

}
package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import java.util.Calendar;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;
    private final KilometryRepository kilometryRepository;
    private final ZuzycieRepository zuzycieRepository;
    private final StanRepository stanRepository;
    private final ZuzycieService zuzycieService;

    public Double getSumaKilometry(String maszynaId, int year, int month, String excludedDocNumber) throws DocumentNotFoundException {

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
        double stan = kilometry != null ? kilometry.getWartosc() : 0;

        return stan + dokumentRepository.findByMaszyna(maszyna).stream().filter(d -> {
            cal.setTime(d.getData());
            return (cal.get(Calendar.MONTH) + 1) == month
                    && cal.get(Calendar.YEAR) == year
                    &&  !d.getNumer().equals(excludedDocNumber)
                    && (dokument == null || calD.get(Calendar.DAY_OF_MONTH) >= cal.get(Calendar.DAY_OF_MONTH));
        }).mapToDouble(Dokument::getKilometry).sum();

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

            for(Zuzycie zuzycie : dokument.getZuzycie()){

                Double suma = null;
                Double sumaBefore = null;
                zuzycie.setDokument(dokument);
                try {
                    suma = zuzycieService.getSuma(zuzycie.getNorma().getId(), year, month, null);
                    sumaBefore = zuzycieService.getSuma(zuzycie.getNorma().getId(), year, month, dokument.getNumer());

                } catch (DocumentNotFoundException e) {
                    e.printStackTrace();
                }

                Stan stan = stanRepository.findOneByNormaAndRokAndMiesiac(zuzycie.getNorma(), year, month);
                double stanD = stan == null ? 0 : stan.getWartosc();

                zuzycie.getNorma().setSuma(suma);
                zuzycie.getNorma().setSumaBefore(sumaBefore);
                zuzycie.getNorma().setStan(stanD);

                zuzycie.setDokument(null);
                zuzycie.getNorma().setMaszyna(null);

            }

            Double kilometryBefore = null;
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
        for(Zuzycie zuzycie : dokument.getZuzycie()){
            zuzycie.setDokument(dokument);
            zuzycieRepository.save(zuzycie);
        }
    }

    public void put(Dokument dokument) throws DocumentNotFoundException {
        Optional<Dokument> byId = dokumentRepository.findById(dokument.getNumer());

        if(byId.isPresent()){
            Dokument dokumentDB = byId.get();

            dokumentDB.setData(dokument.getData());
            dokumentDB.setKilometry(dokument.getKilometry());
            dokumentDB.setKilometryPrzyczepa(dokument.getKilometryPrzyczepa());
            dokumentRepository.save(dokumentDB);

            for(Zuzycie zuzycie : dokument.getZuzycie()) {
                Zuzycie zuzycieDB = zuzycieRepository.findById(zuzycie.getId()).orElse(null);

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

    public void delete(String numer) throws DocumentNotFoundException {

        Optional<Dokument> byId = dokumentRepository.findById(numer);

        if(byId.isPresent()) {
            Dokument dokumentDB = byId.get();
            dokumentRepository.delete(dokumentDB);
        }
        else{
            throw new DocumentNotFoundException(numer);
        }

    }

}
package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;

import java.util.Calendar;

@Component
@RequiredArgsConstructor
public class DokumentService {

    private final DokumentRepository dokumentRepository;
    private final KilometryRepository kilometryRepository;

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

}
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

    public Double getSumaKilometry(String maszynaId, int year, int month){

        Maszyna maszyna = new Maszyna();
        maszyna.setId(maszynaId);
        Calendar cal = Calendar.getInstance();

        Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(maszyna, year, month);
        double stan = kilometry != null ? kilometry.getWartosc() : 0;

        return stan + dokumentRepository.findByMaszyna(maszyna).stream().filter(d -> {
            cal.setTime(d.getData());
            return cal.get(Calendar.MONTH) + 1 == month && cal.get(Calendar.YEAR) == year;
        }).mapToDouble(Dokument::getKilometry).sum();

    }

    public Double getSumaKilometryBeforeDoc(String dokumentNumer) throws DocumentNotFoundException {

        Dokument dokument = dokumentRepository.findById(dokumentNumer)
                .orElseThrow(() -> new DocumentNotFoundException(dokumentNumer));

        Maszyna maszyna = dokument.getMaszyna();

        Calendar calDate = Calendar.getInstance();
        calDate.setTime(dokument.getData());

        Kilometry kilometry = kilometryRepository.findOneByMaszynaAndRokAndMiesiac(maszyna, calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH) +1);
        double stan = kilometry != null ? kilometry.getWartosc() : 0;

        Calendar cal = Calendar.getInstance();
        return stan + dokumentRepository.findByMaszyna(maszyna).stream()
                .filter(d -> {
                    cal.setTime(d.getData());
                    return calDate.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                            && calDate.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                            && calDate.get(Calendar.DAY_OF_MONTH) >= cal.get(Calendar.DAY_OF_MONTH)
                            && !d.getNumer().equals(dokumentNumer);
                }).mapToDouble(Dokument::getKilometry).sum();
    }

}
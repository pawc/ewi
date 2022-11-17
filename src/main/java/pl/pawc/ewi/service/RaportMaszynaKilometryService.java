package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Maszyna;
import pl.pawc.ewi.model.RaportMaszynaKilometry;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.MaszynaRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RaportMaszynaKilometryService {

    private final DokumentRepository dokumentRepository;
    private final MaszynaRepository maszynaRepository;

    public RaportMaszynaKilometry getRaportKilometry(String maszynaId, String dateStartS, String dateEndS) throws ParseException {

        Maszyna maszyna = maszynaRepository.findById(maszynaId).orElse(null);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = formatter.parse(dateStartS);
        Date dateEnd = formatter.parse(dateEndS);

        List<Dokument> documentsByDataBetween = dokumentRepository.findByMaszynaAndDataBetween(maszyna, dateStart, dateEnd);

        BigDecimal sumaKilometry = documentsByDataBetween.stream().map(Dokument::getKilometry).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumaKilometryPrzyczepa = documentsByDataBetween.stream().map(Dokument::getKilometryPrzyczepa).reduce(BigDecimal.ZERO, BigDecimal::add);

        RaportMaszynaKilometry raportMaszynaKilometry = new RaportMaszynaKilometry();
        raportMaszynaKilometry.setMaszyna(maszyna);
        raportMaszynaKilometry.setDataStart(dateStart);
        raportMaszynaKilometry.setDataEnd(dateEnd);
        raportMaszynaKilometry.setDokumenty(documentsByDataBetween);
        raportMaszynaKilometry.setSumaKilometry(sumaKilometry);
        raportMaszynaKilometry.setSumaKilometryPrzyczepa(sumaKilometryPrzyczepa);

        return raportMaszynaKilometry;

    }

}
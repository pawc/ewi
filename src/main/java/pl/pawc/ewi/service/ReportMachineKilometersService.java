package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.model.ReportMachineKilometers;
import pl.pawc.ewi.repository.DocumentRepository;
import pl.pawc.ewi.repository.MachineRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportMachineKilometersService {

    private final DocumentRepository documentRepository;
    private final MachineService machineService;

    public ReportMachineKilometers getRaportKilometry(String maszynaId, String dateStartS, String dateEndS) throws ParseException {

        Machine machine = machineService.findById(maszynaId).orElse(null);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = formatter.parse(dateStartS);
        Date dateEnd = formatter.parse(dateEndS);

        List<Document> documentsByDataBetween = documentRepository.findByMachineAndDateBetween(machine, dateStart, dateEnd);

        BigDecimal sumaKilometry = documentsByDataBetween.stream().map(Document::getKilometers).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumaKilometryPrzyczepa = documentsByDataBetween.stream().map(Document::getKilometersTrailer).reduce(BigDecimal.ZERO, BigDecimal::add);

        ReportMachineKilometers reportMachineKilometers = new ReportMachineKilometers();
        reportMachineKilometers.setMachine(machine);
        reportMachineKilometers.setDataStart(dateStart);
        reportMachineKilometers.setDataEnd(dateEnd);
        reportMachineKilometers.setDocuments(documentsByDataBetween);
        reportMachineKilometers.setSumKilometers(sumaKilometry);
        reportMachineKilometers.setSumKilometersTrailer(sumaKilometryPrzyczepa);

        return reportMachineKilometers;

    }

}
package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.entity.Machine;
import pl.pawc.ewi.model.ReportMachineKilometers;
import pl.pawc.ewi.repository.DocumentRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportMachineKilometersService {

    private final DocumentRepository documentRepository;
    private final MachineService machineService;

    public ReportMachineKilometers getReportKilometers(String machineId, String dateStartS, String dateEndS) throws ParseException {

        Optional<Machine> machineById = machineService.findById(machineId);
        if(machineById.isEmpty()) return null;
        Machine machine = machineById.get();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = formatter.parse(dateStartS);
        Date dateEnd = formatter.parse(dateEndS);

        List<Document> documentsByDataBetween = documentRepository.findByMachineAndDateBetween(machine, dateStart, dateEnd);

        BigDecimal sumKilometers = documentsByDataBetween.stream().map(Document::getKilometers).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumKilometersTrailer = documentsByDataBetween.stream().map(Document::getKilometersTrailer).reduce(BigDecimal.ZERO, BigDecimal::add);

        ReportMachineKilometers reportMachineKilometers = new ReportMachineKilometers();
        reportMachineKilometers.setMachine(machine);
        reportMachineKilometers.setDataStart(dateStart);
        reportMachineKilometers.setDataEnd(dateEnd);
        reportMachineKilometers.setDocuments(documentsByDataBetween);
        reportMachineKilometers.setSumKilometers(sumKilometers);
        reportMachineKilometers.setSumKilometersTrailer(sumKilometersTrailer);

        return reportMachineKilometers;

    }

}
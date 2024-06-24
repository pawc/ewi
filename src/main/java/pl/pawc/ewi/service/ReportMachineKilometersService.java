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
        if (machineById.isEmpty()) return null;

        Machine machine = machineById.get();

        Date dateStart = parseDate(dateStartS);
        Date dateEnd = parseDate(dateEndS);

        List<Document> documentsByDataBetween = documentRepository.findByMachineAndDateBetween(machine, dateStart, dateEnd);

        BigDecimal sumKilometers = getSumKilometers(documentsByDataBetween);
        BigDecimal sumKilometersTrailer = getSumKilometersTrailer(documentsByDataBetween);

        return getReportMachineKilometers(machine, dateStart, dateEnd, documentsByDataBetween, sumKilometers, sumKilometersTrailer);
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(date);
    }

    private static BigDecimal getSumKilometersTrailer(List<Document> documentsByDataBetween) {
        return documentsByDataBetween.stream()
                .map(Document::getKilometersTrailer)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getSumKilometers(List<Document> documentsByDataBetween) {
        return documentsByDataBetween.stream()
                .map(Document::getKilometers)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static ReportMachineKilometers getReportMachineKilometers(Machine machine, Date dateStart, Date dateEnd,
                      List<Document> documentsByDataBetween, BigDecimal sumKilometers, BigDecimal sumKilometersTrailer) {
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
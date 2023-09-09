package pl.pawc.ewi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.*;
import pl.pawc.ewi.entity.Category;
import pl.pawc.ewi.model.AnnualReport;
import pl.pawc.ewi.model.Report;
import pl.pawc.ewi.model.KilometersReport;
import pl.pawc.ewi.repository.DocumentRepository;
import pl.pawc.ewi.repository.KilometersRepository;
import pl.pawc.ewi.repository.FuelConsumptionStandardRepository;
import pl.pawc.ewi.repository.FuelInitialStateRepository;
import pl.pawc.ewi.repository.FuelConsumptionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final KilometersRepository kilometersRepository;
    private final MachineService machineService;
    private final CategoryService categoryService;
    private final FuelConsumptionService fuelConsumptionService;
    private final FuelInitialStateRepository fuelInitialStateRepository;
    private final DocumentRepository documentRepository;
    private final FuelConsumptionStandardRepository fuelConsumptionStandardRepository;
    private final FuelConsumptionRepository fuelConsumptionRepository;

    public List<KilometersReport> getKilometersReport(int rok, int miesiac){

        List<Machine> allActive = machineService.findAllActive();
        List<KilometersReport> result = new ArrayList<>();

        allActive.forEach(m -> {
            KilometersReport kilometersReport = new KilometersReport();
            kilometersReport.setMachineId(m.getId());
            kilometersReport.setMachineName(m.getName());
            Kilometers oneByMaszynaAndRokAndMiesiac = kilometersRepository.findOneByMachineAndYearAndMonth(m, rok, miesiac);
            if (oneByMaszynaAndRokAndMiesiac == null) kilometersReport.setInitialState(BigDecimal.ZERO);
            else kilometersReport.setInitialState(oneByMaszynaAndRokAndMiesiac.getValue());
            result.add(kilometersReport);
        });

        return result;

    }

    public List<AnnualReport> getReportAnnual(int year){
        List<AnnualReport> result = getMachineFuelConsumptionStandardUngrouped(year);
        Map<String, List<AnnualReport>> collect = result.stream().collect(groupingBy(AnnualReport::getCategory_unit));
        List<AnnualReport> groupBy = new ArrayList<>();

        collect.forEach((s, lista) -> {
            BigDecimal sum = lista.stream().map(AnnualReport::getSum).reduce(BigDecimal.ZERO, BigDecimal::add);
            if(BigDecimal.ZERO.equals(sum)) return;

            AnnualReport annualReport = new AnnualReport();
            annualReport.setCategory_unit(s);
            annualReport.setCategory(lista.get(0).getCategory());
            annualReport.setUnit(lista.get(0).getUnit());
            BigDecimal waga = lista.get(0).getWeight();
            annualReport.setWeight(waga);
            BigDecimal suma = sum.setScale(2, RoundingMode.HALF_UP);
            annualReport.setSum(suma);
            annualReport.setSumMultipliedByWeight(suma.multiply(waga));

            groupBy.add(annualReport);

        });

        return groupBy;
    }

    private List<AnnualReport> getMachineFuelConsumptionStandardUngrouped(int year) {
        List<AnnualReport> result = new ArrayList<>();

        for (Category k : categoryService.findAll()) {
            for (Machine m : k.getMachines()) {
                fuelConsumptionStandardRepository.findByMachine(m).forEach(n -> getAnnualReportFuelConsumptionStandardByMachine(year, result, k, n));
            }
        }
        return result;
    }

    private void getAnnualReportFuelConsumptionStandardByMachine(int year, List<AnnualReport> result, Category k, FuelConsumptionStandard n) {
        AnnualReport annualReport = new AnnualReport();

        String jednostka = n.getUnitObj() == null ? n.getUnit() : n.getUnitObj().getName();
        BigDecimal waga = n.getUnitObj() == null ? BigDecimal.ONE : n.getUnitObj().getWeightRatio();

        String kategoriaJednostka = new StringBuilder(k.getName()).append("-").append(jednostka).toString();
        annualReport.setCategory_unit(kategoriaJednostka.toUpperCase());

        annualReport.setCategory(k.getName());
        annualReport.setUnit(jednostka);
        annualReport.setWeight(waga);

        BigDecimal sumaYear = fuelConsumptionService.getSumYear(n.getId(), year);
        annualReport.setSum(sumaYear);

        result.add(annualReport);
    }

    public List<Report> getReport(int year, int month, boolean isQuarterly) {
        Calendar cal = Calendar.getInstance();

        if(isQuarterly) month = getFirstMonthOfQuarter(month);
        cal.set(year, month-1, 1);

        Date firstDayOfMonth = cal.getTime();
        cal.add(Calendar.MONTH, isQuarterly ? 3 : 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = cal.getTime();

        List<Document> dokumentsByDataBetween = documentRepository.findByDateBetween(firstDayOfMonth, lastDayOfMonth);

        Iterable<FuelConsumptionStandard> normy = fuelConsumptionStandardRepository.findAll();

        List<Report> results = new ArrayList<>();

        for (FuelConsumptionStandard fuelConsumptionStandard : normy) {

            List<Document> documentList = dokumentsByDataBetween.stream().filter(d -> d.getMachine().equals(fuelConsumptionStandard.getMachine())).toList();
            if(documentList.isEmpty()) continue;

            int scale = fuelConsumptionStandard.isRounded() ? 2 : 1;

            BigDecimal sumaWartosc = BigDecimal.ZERO;
            BigDecimal sumaOgrzewanie = BigDecimal.ZERO;
            BigDecimal sumaTankowanie = BigDecimal.ZERO;
            BigDecimal sumaKilometry = BigDecimal.ZERO;
            BigDecimal sumaKilometryPrzyczepa = BigDecimal.ZERO;
            BigDecimal sumaGodzin = BigDecimal.ZERO;

            for (Document d : documentList) {
                Optional<FuelConsumption> fuelConsumptionOptional =
                        fuelConsumptionRepository.findByDocument(d).stream()
                        .filter(z -> z.getFuelConsumptionStandard().equals(fuelConsumptionStandard))
                        .findFirst();
                if(fuelConsumptionOptional.isEmpty()) continue;
                FuelConsumption fuelConsumption = fuelConsumptionOptional.get();
                sumaWartosc = sumaWartosc.add(fuelConsumption.getValue().multiply(fuelConsumptionStandard.getValue()).setScale(scale, RoundingMode.HALF_UP));
                sumaOgrzewanie = sumaOgrzewanie.add(fuelConsumption.getHeating()).setScale(scale, RoundingMode.HALF_UP);
                sumaTankowanie = sumaTankowanie.add(fuelConsumption.getRefueled()).setScale(scale, RoundingMode.HALF_UP);
                sumaKilometry = sumaKilometry.add(d.getKilometers());
                sumaKilometryPrzyczepa = sumaKilometryPrzyczepa.add(d.getKilometersTrailer());
                sumaGodzin = sumaGodzin.add(fuelConsumption.getValue()).setScale(scale, RoundingMode.HALF_UP);
            }

            FuelInitialState fuelInitialState = fuelInitialStateRepository.findOneByFuelConsumptionStandardAndYearAndMonth(fuelConsumptionStandard, year, month);
            BigDecimal stanPoprz = fuelInitialState == null ? BigDecimal.ZERO : fuelInitialState.getValue();

            BigDecimal endState = stanPoprz.subtract(sumaWartosc).subtract(sumaOgrzewanie).add(sumaTankowanie).setScale(scale, RoundingMode.HALF_UP);

            Kilometers kilometers = kilometersRepository.findOneByMachineAndYearAndMonth(fuelConsumptionStandard.getMachine(), year, month);
            BigDecimal stanKilometry = kilometers != null ? kilometers.getValue() : BigDecimal.ZERO;
            BigDecimal endStateKilometry = stanKilometry.add(sumaKilometry);

            Report report = new Report();
            report.setMachineIdFuelConsumptionStandardId(fuelConsumptionStandard.getMachine().getId()+"-"+ fuelConsumptionStandard.getId());
            report.setMachine(fuelConsumptionStandard.getMachine().getName() + "(" + fuelConsumptionStandard.getMachine().getId() + ")");
            report.setMachineId(fuelConsumptionStandard.getMachine().getId());
            report.setKilometersInitialState(stanKilometry);
            report.setKilometers(sumaKilometry);
            report.setEndStateKilometers(endStateKilometry);
            report.setKilometersTrailer(sumaKilometryPrzyczepa);
            report.setUnit(fuelConsumptionStandard.getUnitObj() == null ? fuelConsumptionStandard.getUnit() : fuelConsumptionStandard.getUnitObj().getName());
            report.setSum(sumaWartosc);
            report.setSumHours(sumaGodzin);
            report.setRefueled(sumaTankowanie);
            report.setHeating(sumaOgrzewanie);
            report.setFuelConsumptionStandardId(fuelConsumptionStandard.getId());
            report.setInitialState(stanPoprz);
            report.setEndState(endState);
            results.add(report);

        }

        return results;

    }

    private int getFirstMonthOfQuarter(int quarter){
        if(quarter == 1) return 1;
        if(quarter == 2) return 4;
        if(quarter == 3) return 6;
        else return 9;
    }

}
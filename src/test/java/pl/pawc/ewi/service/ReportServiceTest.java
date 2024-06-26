package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.AnnualReport;
import pl.pawc.ewi.model.KilometersReport;
import pl.pawc.ewi.model.Report;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ReportServiceTest {

	@Autowired
	ReportService reportService;

	@Test
	@Transactional
	void testGetKilometersReport() {

		List<KilometersReport> kilometersReport = reportService.getKilometersReport(2022, 4);
		assertEquals(2, kilometersReport.size());

		kilometersReport = reportService.getKilometersReport(2022, 5);
		assertEquals(2, kilometersReport.size());

		for (KilometersReport r : kilometersReport) {
			assertEquals(BigDecimal.ZERO, r.getInitialState());
			assertNotNull(r.getMachineId());
			assertNotNull(r.getMachineName());
			assertNotNull(r.toString());
		}

	}

	@Test
	@Transactional
	void testGetAnnualReport() {

		List<AnnualReport> annualReport = reportService.getReportAnnual(2022);
		assertEquals(2, annualReport.size());
		assertNotNull(annualReport.stream().findFirst().toString());

		AnnualReport rr = annualReport.stream().filter(r -> "L/H".equals(r.getUnit())).findFirst().orElseThrow();
		assertEquals(new BigDecimal("152.76"), rr.getSum());
		assertEquals(new BigDecimal("152.76"), rr.getSumMultipliedByWeight());

		assertEquals(new BigDecimal("35.19"), annualReport.stream().filter(r -> "ON/H".equals(r.getUnit())).findFirst().get().getSum());

		assertEquals(0, reportService.getReportAnnual(2023).size());

	}

	@Test
	@Transactional
	void testGetReport() {

		List<Report> monthlyReport202204 = reportService.getReport(2022, 4, false);
		assertEquals(4, monthlyReport202204.size());
		Report report = monthlyReport202204.stream().filter(r -> "C1-1".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(8.6), report.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(102.9), report.getKilometers());
		assertEquals(BigDecimal.valueOf(121.2), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(19.1), report.getSum());
		assertEquals(BigDecimal.valueOf(44.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(18.7), report.getHeating());
		assertEquals(BigDecimal.valueOf(15.5), report.getSumHours());
		assertEquals(BigDecimal.valueOf(222.9), report.getEndStateKilometers());

		report = monthlyReport202204.stream().filter(r -> "C1-2".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(6.9), report.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(102.9), report.getKilometers());
		assertEquals(BigDecimal.valueOf(121.2), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(116.3), report.getSum());
		assertEquals(BigDecimal.valueOf(122.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(25.5), report.getSumHours());
		assertEquals(BigDecimal.valueOf(222.9), report.getEndStateKilometers());

		report = monthlyReport202204.stream().filter(r -> "W2-3".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(3.5), report.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(91.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(68.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(42.0), report.getSum());
		assertEquals(BigDecimal.valueOf(65.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(25.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(10.0), report.getSumHours());
		assertEquals(BigDecimal.valueOf(141.0), report.getEndStateKilometers());

		report = monthlyReport202204.stream().filter(r -> "W2-4".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(20.5), report.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(91.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(68.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(20.6), report.getSum());
		assertEquals(BigDecimal.valueOf(29.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(17.1), report.getSumHours());
		assertEquals(BigDecimal.valueOf(141.0), report.getEndStateKilometers());

		List<Report> monthlyReport202205 = reportService.getReport(2022, 5, false);
		assertEquals(4, monthlyReport202205.size());

		report = monthlyReport202205.stream().filter(r -> "C1-1".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().get();
		assertEquals(BigDecimal.valueOf(16.9), report.getEndState());
		assertEquals(BigDecimal.valueOf(0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(58.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(41.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(16.1), report.getSum());
		assertEquals(BigDecimal.valueOf(57.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(24.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(13.1), report.getSumHours());
		assertEquals(BigDecimal.valueOf(58.0), report.getEndStateKilometers());

		report = monthlyReport202205.stream().filter(r -> "C1-2".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(30.5), report.getEndState());
		assertEquals(BigDecimal.valueOf(0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(58.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(41.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(36.5), report.getSum());
		assertEquals(BigDecimal.valueOf(67.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(8.0), report.getSumHours());
		assertEquals(BigDecimal.valueOf(58.0), report.getEndStateKilometers());

		report =  monthlyReport202205.stream().filter(r -> "W2-3".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(1.3), report.getEndState());
		assertEquals(BigDecimal.valueOf(0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(61.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(48.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(70.7), report.getSum());
		assertEquals(BigDecimal.valueOf(86.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(14.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(16.8), report.getSumHours());
		assertEquals(BigDecimal.valueOf(61.0), report.getEndStateKilometers());

		report = monthlyReport202205.stream().filter(r -> "W2-4".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(17.2), report.getEndState());
		assertEquals(BigDecimal.valueOf(0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(61.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(48.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(27.8), report.getSum());
		assertEquals(BigDecimal.valueOf(45.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(23.1), report.getSumHours());
		assertEquals(BigDecimal.valueOf(61.0), report.getEndStateKilometers());

		assertEquals(0, reportService.getReport(2023, 5, false).size());

		List<Report> q2Report2022 = reportService.getReport(2022, 2, true);
		assertEquals(4, q2Report2022.size());

		report = q2Report2022.stream().filter(r -> "C1-1".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(25.5), report.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(160.9), report.getKilometers());
		assertEquals(BigDecimal.valueOf(162.2), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(35.2), report.getSum());
		assertEquals(BigDecimal.valueOf(101.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(42.7), report.getHeating());
		assertEquals(BigDecimal.valueOf(28.6), report.getSumHours());
		assertEquals(BigDecimal.valueOf(280.9), report.getEndStateKilometers());

		report = q2Report2022.stream().filter(r -> "C1-2".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(37.4), report.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(160.9), report.getKilometers());
		assertEquals(BigDecimal.valueOf(162.2), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(152.8), report.getSum());
		assertEquals(BigDecimal.valueOf(189.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(33.5), report.getSumHours());
		assertEquals(BigDecimal.valueOf(280.9), report.getEndStateKilometers());

		report = q2Report2022.stream().filter(r -> "W2-3".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(4.8), report.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(152.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(116.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(112.7), report.getSum());
		assertEquals(BigDecimal.valueOf(151.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(39.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(26.8), report.getSumHours());
		assertEquals(BigDecimal.valueOf(202.0), report.getEndStateKilometers());

		report = q2Report2022.stream().filter(r -> "W2-4".equals(r.getMachineIdFuelConsumptionStandardId())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(37.7), report.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), report.getKilometersInitialState());
		assertEquals(BigDecimal.valueOf(152.0), report.getKilometers());
		assertEquals(BigDecimal.valueOf(116.0), report.getKilometersTrailer());
		assertEquals(BigDecimal.valueOf(48.4), report.getSum());
		assertEquals(BigDecimal.valueOf(74.0), report.getRefueled());
		assertEquals(BigDecimal.valueOf(0.0), report.getHeating());
		assertEquals(BigDecimal.valueOf(40.2), report.getSumHours());
		assertEquals(BigDecimal.valueOf(202.0), report.getEndStateKilometers());

		assertEquals(0, reportService.getReport(2022, 1, true).size());

	}

}
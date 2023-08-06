package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.ReportMachineKilometers;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ReportMachineKilometersServiceTest {

	@Autowired
	ReportMachineKilometersService raportKilometryService;

	@Test
	@Transactional
	void testRaportMaszynaKilometry() throws ParseException {

		ReportMachineKilometers reportMachineKilometers = raportKilometryService.getRaportKilometry("C1", "2022-04-01", "2022-04-30");
		assertEquals(new BigDecimal("102.9"), reportMachineKilometers.getSumKilometers());
		assertEquals(new BigDecimal("121.2"), reportMachineKilometers.getSumKilometersTrailer());
		assertEquals(10, reportMachineKilometers.getDocuments().size());
		assertNotNull(reportMachineKilometers.toString());

	}

}
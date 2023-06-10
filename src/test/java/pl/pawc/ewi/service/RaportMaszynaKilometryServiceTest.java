package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.RaportMaszynaKilometry;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RaportMaszynaKilometryServiceTest {

	@Autowired
	RaportMaszynaKilometryService raportKilometryService;

	@Test
	@Transactional
	void testRaportMaszynaKilometry() throws ParseException {

		RaportMaszynaKilometry raportMaszynaKilometry = raportKilometryService.getRaportKilometry("C1", "2022-04-01", "2022-04-30");
		assertEquals(new BigDecimal("102.9"), raportMaszynaKilometry.getSumaKilometry());
		assertEquals(new BigDecimal("121.2"), raportMaszynaKilometry.getSumaKilometryPrzyczepa());
		assertEquals(10, raportMaszynaKilometry.getDokumenty().size());
		assertNotNull(raportMaszynaKilometry.toString());

	}

}
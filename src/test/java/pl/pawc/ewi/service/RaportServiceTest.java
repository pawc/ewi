package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.RaportKilometry;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class RaportServiceTest {

	@Autowired
	RaportService raportService;

	@Test
	@Transactional
	void testGetKilometryRaport() {

		List<RaportKilometry> kilometryRaport = raportService.getKilometryRaport(2022, 4);
		assertEquals(2, kilometryRaport.size());

		kilometryRaport = raportService.getKilometryRaport(2022, 5);
		assertEquals(2, kilometryRaport.size());

		for (RaportKilometry r : kilometryRaport) {
			assertEquals(0, r.getStanpoczatkowy());
		}

	}

	@Test
	@Transactional
	void testGetRaportRoczny() {

		assertEquals(3, raportService.getRaportRoczny(2022).size());
		assertEquals(0, raportService.getRaportRoczny(2023).size());

	}

	@Test
	@Transactional
	void testGetRaport() {

		assertEquals(3, raportService.getRaport(2022, 4).size());
		assertEquals(0, raportService.getRaport(2022, 5).size());

	}

}
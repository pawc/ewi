package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Zuzycie;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.ZuzycieRepository;

import javax.transaction.Transactional;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class ZuzycieServiceTest {

	@Autowired
	ZuzycieService zuzycieService;

	@Autowired
	ZuzycieRepository zuzycieRepository;

	@Autowired
	DokumentRepository dokumentRepository;

	@Test
	@Transactional
	void testGetSuma() {

		try {
			assertEquals(8.6, zuzycieService.getSuma(1, 2022, 4, null));
			assertEquals(6.9, zuzycieService.getSuma(2, 2022, 4, null));
			assertEquals(3.5, zuzycieService.getSuma(3, 2022, 4, null));
			assertEquals(20.5, zuzycieService.getSuma(4, 2022, 4, null));
			assertEquals(16.9, zuzycieService.getSuma(1, 2022, 5, null));
			assertEquals(30.5, zuzycieService.getSuma(2, 2022, 5, null));
			assertEquals(1.3, zuzycieService.getSuma(3, 2022, 5, null));
			assertEquals(17.2, zuzycieService.getSuma(4, 2022, 5, null));

		} catch (DocumentNotFoundException e) {
			fail();
		}

	}

	@Test
	@Transactional
	void testGetSumaDocExcluded() throws DocumentNotFoundException {
		Iterable<Zuzycie> all = zuzycieRepository.findAll();
		Calendar cal = Calendar.getInstance();

		for(Zuzycie z : all){
			cal.setTime(z.getDokument().getData());
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;

			Double expected = dokumentRepository.getSumBeforeDate(z.getNorma().getId(), year , month,
					z.getDokument().getData(), z.getDokument().getNumer());
			Double actual = zuzycieService.getSuma(z.getNorma().getId(), year, month, z.getDokument().getNumer());

			assertEquals(expected, actual);

		}

		assertThrows(DocumentNotFoundException.class,
				() -> zuzycieService.getSuma(1, 2022, 4,"11/04/2025/C1"));


	}

/*	@Test
	@Transactional
	void testGetSumaYear(){


		assertEquals(385.48, zuzycieService.getSumaYear(1, 2022));
		assertEquals(0, zuzycieService.getSumaYear(1, 2023));

		assertEquals(56.63, zuzycieService.getSumaYear(2, 2022));
		assertEquals(0, zuzycieService.getSumaYear(2, 2023));

		assertEquals(18.37, zuzycieService.getSumaYear(26, 2022));
		assertEquals(0, zuzycieService.getSumaYear(26, 2023));

	}*/

}
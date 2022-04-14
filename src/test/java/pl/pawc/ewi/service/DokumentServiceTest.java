package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.DocumentNotFoundException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class DokumentServiceTest {

	@Autowired
	DokumentService dokumentService;

	@Test
	@Transactional
	void testGetSumaKilometry() {

		assertEquals(261, dokumentService.getSumaKilometry("C1", 2022, 4));
		assertEquals(0, dokumentService.getSumaKilometry("C11", 2022, 4));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			assertEquals(55, dokumentService.getSumaKilometryBeforeDoc("1/04/2022/C1"));
			assertEquals(65, dokumentService.getSumaKilometryBeforeDoc("2/04/2022/C1"));
			assertEquals(85, dokumentService.getSumaKilometryBeforeDoc("3/04/2022/C1"));
			assertEquals(115, dokumentService.getSumaKilometryBeforeDoc("4/04/2022/C1"));
			assertEquals(136, dokumentService.getSumaKilometryBeforeDoc("5/04/2022/C1"));
			assertEquals(136, dokumentService.getSumaKilometryBeforeDoc("6/04/2022/C1"));
			assertEquals(163.5, dokumentService.getSumaKilometryBeforeDoc("7/04/2022/C1"));
			assertEquals(204.5, dokumentService.getSumaKilometryBeforeDoc("8/04/2022/C1"));
			assertEquals(224.8, dokumentService.getSumaKilometryBeforeDoc("9/04/2022/C1"));
			assertEquals(243.8, dokumentService.getSumaKilometryBeforeDoc("10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			e.printStackTrace();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> dokumentService.getSumaKilometryBeforeDoc("11/04/2022/C1"));

	}
}
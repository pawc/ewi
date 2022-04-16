package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.controller.DokumentRestController;
import pl.pawc.ewi.model.DocumentNotFoundException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class DokumentServiceTest {

	@Autowired
	DokumentService dokumentService;

	@Autowired
	DokumentRestController dokumentRestController;

	@Test
	@Transactional
	void testGetSumaKilometry() throws DocumentNotFoundException {

		assertEquals(261, dokumentService.getSumaKilometry("C1", 2022, 4, null));
		assertEquals(0, dokumentService.getSumaKilometry("C11", 2022, 4, null));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			assertEquals(55, dokumentService.getSumaKilometry("C1", 2022, 4,"1/04/2022/C1"));
			assertEquals(65, dokumentService.getSumaKilometry("C1", 2022, 4,"2/04/2022/C1"));
			assertEquals(85, dokumentService.getSumaKilometry("C1", 2022, 4,"3/04/2022/C1"));
			assertEquals(115, dokumentService.getSumaKilometry("C1", 2022, 4,"4/04/2022/C1"));
			assertEquals(136, dokumentService.getSumaKilometry("C1", 2022, 4,"5/04/2022/C1"));
			assertEquals(136, dokumentService.getSumaKilometry("C1", 2022, 4,"6/04/2022/C1"));
			assertEquals(163.5, dokumentService.getSumaKilometry("C1", 2022, 4,"7/04/2022/C1"));
			assertEquals(204.5, dokumentService.getSumaKilometry("C1", 2022, 4,"8/04/2022/C1"));
			assertEquals(224.8, dokumentService.getSumaKilometry("C1", 2022, 4,"9/04/2022/C1"));
			assertEquals(243.8, dokumentService.getSumaKilometry("C1", 2022, 4,"10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			fail();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> dokumentService.getSumaKilometry("C1", 2022, 4,"11/04/2022/C1"));

	}

	@Test
	@Transactional
	void testGetDokument() throws DocumentNotFoundException {

		assertNotNull(dokumentService.get("1/04/2022/C1"));

	}

}
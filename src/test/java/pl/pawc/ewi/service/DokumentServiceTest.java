package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Dokument;
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
	void testPostGetDokument() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"numer\":\"15/04/2022/C1\",\"data\":\"2022-04-17\",\"kilometry\":\"25\",\"kilometryPrzyczepa\":\"25\",\"maszyna\":{\"id\":\"C1\"},\"zuzycie\":[{\"wartosc\":\"2.5\",\"norma\":{\"id\":\"1\"},\"zatankowano\":\"2.5\",\"ogrzewanie\":\"2.5\"},{\"wartosc\":\"3.5\",\"norma\":{\"id\":\"2\"},\"zatankowano\":\"3.5\",\"ogrzewanie\":\"3.5\"}]}";
		Dokument dokument = objectMapper.readValue(input, Dokument.class);
		dokumentService.post(dokument);
		assertNotNull(dokumentService.get("15/04/2022/C1"));

	}

}
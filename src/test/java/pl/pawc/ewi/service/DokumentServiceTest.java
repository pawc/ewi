package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class DokumentServiceTest {

	@Autowired
	DokumentService dokumentService;

	@Autowired
	DokumentRepository dokumentRepository;

	@Autowired
	KilometryRepository kilometryRepository;

	@Test
	@Transactional
	void testGetSumaKilometry() throws DocumentNotFoundException {

		for (Kilometry kilometry : kilometryRepository.findAll()) {
			Double expected = dokumentRepository.getSumaKilometry(kilometry.getMaszyna().getId(), kilometry.getRok(), kilometry.getMiesiac());
			Double actual = dokumentService.getSumaKilometry(kilometry.getMaszyna().getId(), kilometry.getRok(), kilometry.getMiesiac(), null);
			assertEquals(expected, actual);

		}

		assertEquals(0, dokumentService.getSumaKilometry("C11", 2022, 4, null));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			assertEquals(120, dokumentService.getSumaKilometry("C1", 2022, 4,"1/04/2022/C1"));
			assertEquals(130, dokumentService.getSumaKilometry("C1", 2022, 4,"2/04/2022/C1"));
			assertEquals(141, dokumentService.getSumaKilometry("C1", 2022, 4,"3/04/2022/C1"));
			assertEquals(141, dokumentService.getSumaKilometry("C1", 2022, 4,"4/04/2022/C1"));
			assertEquals(146, dokumentService.getSumaKilometry("C1", 2022, 4,"5/04/2022/C1"));
			assertEquals(147.4, dokumentService.getSumaKilometry("C1", 2022, 4,"6/04/2022/C1"));
			assertEquals(154.4, dokumentService.getSumaKilometry("C1", 2022, 4,"7/04/2022/C1"));
			assertEquals(172.9, dokumentService.getSumaKilometry("C1", 2022, 4,"8/04/2022/C1"));
			assertEquals(190.9, dokumentService.getSumaKilometry("C1", 2022, 4,"9/04/2022/C1"));
			assertEquals(209.9, dokumentService.getSumaKilometry("C1", 2022, 4,"10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			fail();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> dokumentService.getSumaKilometry("C1", 2022, 4,"11/04/2022/C1"));

	}

/*	@Test
	@Transactional
	void testPostGetPutDeleteDokument() throws JsonProcessingException, DocumentNotFoundException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"numer\":\"15/04/2022/C1\",\"data\":\"2022-04-17\",\"kilometry\":\"25\",\"kilometryPrzyczepa\":\"25\",\"maszyna\":{\"id\":\"C1\"},\"zuzycie\":[{\"wartosc\":\"2.5\",\"norma\":{\"id\":\"1\"},\"zatankowano\":\"2.5\",\"ogrzewanie\":\"2.5\"},{\"wartosc\":\"3.5\",\"norma\":{\"id\":\"2\"},\"zatankowano\":\"3.5\",\"ogrzewanie\":\"3.5\"}]}";
		Dokument dokument = objectMapper.readValue(input, Dokument.class);
		dokumentService.post(dokument);
		Dokument dokument1 = dokumentService.get("15/04/2022/C1");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dokument1.getData());

		assertNotNull(dokument1);
		assertEquals(25, dokument1.getKilometry());
		assertEquals(25, dokument1.getKilometryPrzyczepa());
		assertEquals(2, dokument1.getZuzycie().size());
		assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(2022, cal.get(Calendar.YEAR));

		input = "{\"numer\":\"15/04/2022/C1\",\"data\":\"2023-05-29\",\"kilometry\":\"9\",\"kilometryPrzyczepa\":\"9\",\"maszyna\":{\"id\":\"C1\"},\"zuzycie\":[{\"wartosc\":\"9\",\"id\":\"1\",\"norma\":{\"id\":\"1\"},\"zatankowano\":\"9\",\"ogrzewanie\":\"9\"},{\"wartosc\":\"9\",\"id\":\"2\",\"norma\":{\"id\":\"2\"},\"zatankowano\":\"9\",\"ogrzewanie\":\"0\"}]}";
		dokument = objectMapper.readValue(input, Dokument.class);
		dokumentService.put(dokument);
		dokument1 = dokumentService.get("15/04/2022/C1");
		cal.setTime(dokument1.getData());

		assertEquals(9, dokument1.getKilometry());
		assertEquals(9, dokument1.getKilometryPrzyczepa());
		assertEquals(2, dokument1.getZuzycie().size());
		assertEquals(29, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(2023, cal.get(Calendar.YEAR));

		dokumentService.delete("15/04/2022/C1");

		assertThrows(DocumentNotFoundException.class, () -> {
			dokumentService.delete("15/04/2022/C1");
		});

	}*/

}
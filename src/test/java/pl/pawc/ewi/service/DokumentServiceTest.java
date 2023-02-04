package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.entity.Kilometry;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.KilometryRepository;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;

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
			BigDecimal actual = dokumentService.getSumaKilometry(kilometry.getMaszyna().getId(), kilometry.getRok(), kilometry.getMiesiac(), null);
			Assertions.assertEquals(BigDecimal.valueOf(expected), actual);

		}

		Assertions.assertEquals(BigDecimal.ZERO, dokumentService.getSumaKilometry("C11", 2022, 4, null));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			Assertions.assertEquals(new BigDecimal("120.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"1/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("130.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"2/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("141.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"3/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("141.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"4/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("146.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"5/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("147.4"), dokumentService.getSumaKilometry("C1", 2022, 4,"6/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("154.4"), dokumentService.getSumaKilometry("C1", 2022, 4,"7/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("172.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"8/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("190.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"9/04/2022/C1"));
			Assertions.assertEquals(new BigDecimal("209.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			Assertions.fail();
		}

		Assertions.assertThrows(DocumentNotFoundException.class,
				() -> dokumentService.getSumaKilometry("C1", 2022, 4,"11/04/2022/C1"));

	}

	@Test
	@Transactional
	void testPostGetPutDeleteDokument() throws JsonProcessingException, DocumentNotFoundException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"numer\":\"15/04/2022/C1\",\"data\":\"2022-04-17\",\"kilometry\":\"25\",\"kilometryPrzyczepa\":\"25\",\"maszyna\":{\"id\":\"C1\"},\"zuzycie\":[{\"wartosc\":\"2.5\",\"norma\":{\"id\":\"1\",\"wartosc\":\"1\"},\"zatankowano\":\"2.5\",\"ogrzewanie\":\"2.5\"},{\"wartosc\":\"3.5\",\"norma\":{\"id\":\"2\",\"wartosc\":\"2\"},\"zatankowano\":\"3.5\",\"ogrzewanie\":\"3.5\"}]}";
		Dokument dokument = objectMapper.readValue(input, Dokument.class);
		dokumentService.post(dokument);
		Dokument dokument1 = dokumentService.get("15/04/2022/C1");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dokument1.getData());

		Assertions.assertNotNull(dokument1);
		Assertions.assertEquals(new BigDecimal("25"), dokument1.getKilometry());
		Assertions.assertEquals(new BigDecimal("25"), dokument1.getKilometryPrzyczepa());
		Assertions.assertEquals(2, dokument1.getZuzycie().size());
		Assertions.assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
		Assertions.assertEquals(3, cal.get(Calendar.MONTH));
		Assertions.assertEquals(2022, cal.get(Calendar.YEAR));

		dokumentService.delete("15/04/2022/C1");

		Assertions.assertThrows(DocumentNotFoundException.class, () -> dokumentService.delete("15/04/2022/C1"));

	}

}
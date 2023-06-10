package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Dokument;
import pl.pawc.ewi.model.DocumentNotFoundException;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DokumentServiceTest {

	@Autowired
	DokumentService dokumentService;

	@Test
	@Transactional
	void testGetSumaKilometry() throws DocumentNotFoundException {

		assertEquals(BigDecimal.ZERO, dokumentService.getSumaKilometry("C11", 2022, 4, null));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			assertEquals(new BigDecimal("120.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"1/04/2022/C1"));
			assertEquals(new BigDecimal("130.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"2/04/2022/C1"));
			assertEquals(new BigDecimal("141.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"3/04/2022/C1"));
			assertEquals(new BigDecimal("141.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"4/04/2022/C1"));
			assertEquals(new BigDecimal("146.0"), dokumentService.getSumaKilometry("C1", 2022, 4,"5/04/2022/C1"));
			assertEquals(new BigDecimal("147.4"), dokumentService.getSumaKilometry("C1", 2022, 4,"6/04/2022/C1"));
			assertEquals(new BigDecimal("154.4"), dokumentService.getSumaKilometry("C1", 2022, 4,"7/04/2022/C1"));
			assertEquals(new BigDecimal("172.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"8/04/2022/C1"));
			assertEquals(new BigDecimal("190.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"9/04/2022/C1"));
			assertEquals(new BigDecimal("209.9"), dokumentService.getSumaKilometry("C1", 2022, 4,"10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			fail();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> dokumentService.getSumaKilometry("C1", 2022, 4,"11/04/2022/C1"));

	}

	@Test
	@Transactional
	void testPostGetPutDeleteDokument() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"numer\":\"15/04/2022/C1\",\"data\":\"2022-04-17\",\"kilometry\":\"25\",\"kilometryPrzyczepa\":\"25\",\"maszyna\":{\"id\":\"C1\"},\"zuzycie\":[{\"wartosc\":\"2.5\",\"norma\":{\"id\":\"1\",\"wartosc\":\"1\"},\"zatankowano\":\"2.5\",\"ogrzewanie\":\"2.5\"},{\"wartosc\":\"3.5\",\"norma\":{\"id\":\"2\",\"wartosc\":\"2\"},\"zatankowano\":\"3.5\",\"ogrzewanie\":\"3.5\"}]}";
		Dokument dokument = objectMapper.readValue(input, Dokument.class);
		dokumentService.post(dokument);
		Dokument dokument1 = dokumentService.get("15/04/2022/C1");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dokument1.getData());

		assertNotNull(dokument1);
		assertEquals(new BigDecimal("25"), dokument1.getKilometry());
		assertEquals(new BigDecimal("25"), dokument1.getKilometryPrzyczepa());
		assertEquals(2, dokument1.getZuzycie().size());
		assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(2022, cal.get(Calendar.YEAR));

		dokumentService.delete("15/04/2022/C1");

	}

}
package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Document;
import pl.pawc.ewi.model.DocumentNotFoundException;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocumentServiceTest {

	@Autowired
	DocumentService documentService;

	@Test
	@Transactional
	void testGetSumaKilometry() throws DocumentNotFoundException {

		assertEquals(BigDecimal.ZERO, documentService.getSumaKilometry("C11", 2022, 4, null));

	}

	@Test
	@Transactional
	void testGetSumaKilometryBeforeDate() {

		try {
			assertEquals(new BigDecimal("120.0"), documentService.getSumaKilometry("C1", 2022, 4,"1/04/2022/C1"));
			assertEquals(new BigDecimal("130.0"), documentService.getSumaKilometry("C1", 2022, 4,"2/04/2022/C1"));
			assertEquals(new BigDecimal("141.0"), documentService.getSumaKilometry("C1", 2022, 4,"3/04/2022/C1"));
			assertEquals(new BigDecimal("141.0"), documentService.getSumaKilometry("C1", 2022, 4,"4/04/2022/C1"));
			assertEquals(new BigDecimal("146.0"), documentService.getSumaKilometry("C1", 2022, 4,"5/04/2022/C1"));
			assertEquals(new BigDecimal("147.4"), documentService.getSumaKilometry("C1", 2022, 4,"6/04/2022/C1"));
			assertEquals(new BigDecimal("154.4"), documentService.getSumaKilometry("C1", 2022, 4,"7/04/2022/C1"));
			assertEquals(new BigDecimal("172.9"), documentService.getSumaKilometry("C1", 2022, 4,"8/04/2022/C1"));
			assertEquals(new BigDecimal("190.9"), documentService.getSumaKilometry("C1", 2022, 4,"9/04/2022/C1"));
			assertEquals(new BigDecimal("209.9"), documentService.getSumaKilometry("C1", 2022, 4,"10/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			fail();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> documentService.getSumaKilometry("C1", 2022, 4,"11/04/2022/C1"));

	}

	@Test
	@Transactional
	void testPostGetPutDeleteDokument() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"number\":\"15/04/2022/C1\",\"date\":\"2022-04-17\",\"kilometers\":\"25\",\"kilometersTrailer\":\"25\",\"machine\":{\"id\":\"C1\"},\"fuelConsumption\":[{\"value\":\"2.5\",\"fuelConsumptionStandard\":{\"id\":\"1\",\"value\":\"1\"},\"refueled\":\"2.5\",\"heating\":\"2.5\"},{\"value\":\"3.5\",\"fuelConsumptionStandard\":{\"id\":\"2\",\"value\":\"2\"},\"refueled\":\"3.5\",\"heating\":\"3.5\"}]}";
		Document document = objectMapper.readValue(input, Document.class);
		documentService.post(document);
		Document document1 = documentService.get("15/04/2022/C1");
		Calendar cal = Calendar.getInstance();
		cal.setTime(document1.getDate());

		assertNotNull(document1);
		assertEquals(new BigDecimal("25"), document1.getKilometers());
		assertEquals(new BigDecimal("25"), document1.getKilometersTrailer());
		assertEquals(2, document1.getFuelConsumption().size());
		assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(2022, cal.get(Calendar.YEAR));

		documentService.delete("15/04/2022/C1");

	}

	@Test
	@Transactional
	void testFindOneById(){
		assertFalse(documentService.findById("3/04/2022/C1").isEmpty());
		assertTrue(documentService.findById("3/04/20223/C1").isEmpty());
	}

}
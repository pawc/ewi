package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Maszyna;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MaszynaServiceTest {

	@Autowired
	MaszynaService maszynaService;

	@Test
	@Transactional
	void testFindAllActive() {

		assertEquals(2, maszynaService.findAllActive().size());

	}

	@Test
	@Transactional
	void testFindAllUncategorized() {

		assertEquals(1, maszynaService.findAllUncategorized().size());

	}

	@Test
	@Transactional
	void testPost() throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"id\":\"ABC123\",\"nazwa\":\"Machine 1\",\"opis\":\"test machine\",\"normy\":[{\"wartosc\":\"1.2\",\"jednostka\":\"L/H\",\"czyOgrzewanie\":false},{\"wartosc\":\"3.27\",\"jednostka\":\"ON/H\",\"czyOgrzewanie\":true}],\"kategorie\":[],\"aktywna\":true}";
		Maszyna maszyna = objectMapper.readValue(input, Maszyna.class);

		Maszyna m = maszynaService.post(maszyna);

		assertEquals(2, m.getNormy().size());
		assertTrue(m.isAktywna());
		m.getNormy().forEach(n -> assertEquals("ABC123", n.getMaszyna().getId()));

	}

	@Test
	@Transactional
	void testGetPut() {

		Maszyna c1 = maszynaService.get("C1", "2022-05");
		assertNotNull(c1);
		assertNotNull(c1.getSumaKilometry());
		c1.getNormy().forEach(n -> assertNotNull(n.getSuma()));
		assertEquals("Ciagnik 1", c1.getNazwa());
		c1.setNazwa("Ciągnik Test");
		maszynaService.put(c1);
		c1 = maszynaService.get("C1", "2022-05");
		assertNotNull(c1);
		assertEquals("Ciągnik Test", c1.getNazwa());

	}

}
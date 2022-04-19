package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Maszyna;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
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
	void testGetPostPut() throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"id\":\"ABC123\",\"nazwa\":\"Machine 1\",\"opis\":\"test machine\",\"normy\":[{\"wartosc\":\"1.2\",\"jednostka\":\"L/H\",\"czyOgrzewanie\":false},{\"wartosc\":\"3.27\",\"jednostka\":\"ON/H\",\"czyOgrzewanie\":true}],\"kategorie\":[],\"aktywna\":true}";
		Maszyna maszyna = objectMapper.readValue(input, Maszyna.class);

		Maszyna m = maszynaService.post(maszyna);

		assertEquals(2, m.getNormy().size());
		assertTrue(m.isAktywna());
		m.getNormy().forEach(n -> {
			assertNotNull(n.getId());
			assertEquals("ABC123", n.getMaszyna().getId());
		});

/*		input = "{\"id\":\"ABC123\",\"nazwa\":\"Machine 2\",\"opis\":\"test machine 2\",\"normy\":[{\"id\":1,\"wartosc\":\"1.2\",\"jednostka\":\"L/H\",\"czyOgrzewanie\":false},{\"id\":2,\"wartosc\":\"3.27\",\"jednostka\":\"ON/H\",\"czyOgrzewanie\":true},{\"wartosc\":\"4.89\",\"jednostka\":\"K/H\",\"czyOgrzewanie\":true}],\"kategorie\":[],\"aktywna\":false}";
		maszyna = objectMapper.readValue(input, Maszyna.class);
		maszynaService.put(maszyna);

		m = maszynaService.get("ABC123", null);
		assertEquals(3, m.getNormy().size());
		assertEquals("Machine 2", m.getNazwa());
		assertEquals("test machine 2", m.getOpis());
		assertFalse(m.isAktywna());*/

	}

}
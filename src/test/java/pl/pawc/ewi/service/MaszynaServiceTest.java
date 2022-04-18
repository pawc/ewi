package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Maszyna;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		assertEquals(0, maszynaService.findAllUncategorized().size());

	}

	@Test
	@Transactional
	void testPost() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"id\":\"ABC123\",\"nazwa\":\"Machine 1\",\"opis\":\"test machine\",\"normy\":[{\"wartosc\":\"1.2\",\"jednostka\":\"L/H\",\"czyOgrzewanie\":false},{\"wartosc\":\"3.27\",\"jednostka\":\"ON/H\",\"czyOgrzewanie\":true}],\"kategorie\":[],\"aktywna\":true}";
		Maszyna maszyna = objectMapper.readValue(input, Maszyna.class);

		Maszyna m = maszynaService.post(maszyna);

		assertEquals(2, m.getNormy().size());
		m.getNormy().forEach(n -> {
			assertNotNull(n.getId());
			assertEquals(maszyna.getId(), n.getMaszyna().getId());
		});
	}

}
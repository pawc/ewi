package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
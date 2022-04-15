package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.DocumentNotFoundException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class ZuzycieServiceTest {

	@Autowired
	ZuzycieService zuzycieService;

	@Test
	@Transactional
	void testGetSuma() {

		try {
			assertEquals(1, zuzycieService.getSuma(1, 2022, 4, null));
			assertEquals(21, zuzycieService.getSuma(2, 2022, 4, null));
			assertEquals(0.2, zuzycieService.getSuma(26, 2022, 4, null));
		} catch (DocumentNotFoundException e) {
			fail();
		}

	}

	@Test
	@Transactional
	void testGetSumaDocExcluded(){
		try {
			assertEquals(10, zuzycieService.getSuma(1, 2022, 4, "1/04/2022/C1"));
			assertEquals(3.3, zuzycieService.getSuma(1, 2022, 4, "2/04/2022/C1"));
			assertEquals(3.5, zuzycieService.getSuma(1, 2022, 4, "3/04/2022/C1"));
			assertEquals(2.5, zuzycieService.getSuma(1, 2022, 4, "4/04/2022/C1"));
			assertEquals(0.9, zuzycieService.getSuma(1, 2022, 4, "5/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			fail();
		}

		assertThrows(DocumentNotFoundException.class,
				() -> zuzycieService.getSuma(1, 2022, 4,"11/04/2022/C1"));

	}

	@Test
	@Transactional
	void testGetSumaYear(){

		assertEquals(385.48, zuzycieService.getSumaYear(1, 2022));
		assertEquals(0, zuzycieService.getSumaYear(1, 2023));

		assertEquals(56.63, zuzycieService.getSumaYear(2, 2022));
		assertEquals(0, zuzycieService.getSumaYear(2, 2023));

		assertEquals(18.37, zuzycieService.getSumaYear(26, 2022));
		assertEquals(0, zuzycieService.getSumaYear(26, 2023));

	}

}
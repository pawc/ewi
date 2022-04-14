package pl.pawc.ewi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.DocumentNotFoundException;

import javax.transaction.Transactional;

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
			Assertions.assertEquals(1, zuzycieService.getSuma(1, 2022, 4, null));
			Assertions.assertEquals(21, zuzycieService.getSuma(2, 2022, 4, null));
			Assertions.assertEquals(0.2, zuzycieService.getSuma(26, 2022, 4, null));
		} catch (DocumentNotFoundException e) {
			Assertions.fail();
		}

	}

	@Test
	@Transactional
	void testGetSumaDocExcluded(){
		try {
			Assertions.assertEquals(10, zuzycieService.getSuma(1, 2022, 4, "1/04/2022/C1"));
			Assertions.assertEquals(3.3, zuzycieService.getSuma(1, 2022, 4, "2/04/2022/C1"));
			Assertions.assertEquals(3.5, zuzycieService.getSuma(1, 2022, 4, "3/04/2022/C1"));
			Assertions.assertEquals(2.5, zuzycieService.getSuma(1, 2022, 4, "4/04/2022/C1"));
			Assertions.assertEquals(0.9, zuzycieService.getSuma(1, 2022, 4, "5/04/2022/C1"));
		} catch (DocumentNotFoundException e) {
			Assertions.fail();
		}

		Assertions.assertThrows(DocumentNotFoundException.class,
				() -> zuzycieService.getSuma(1, 2022, 4,"11/04/2022/C1"));

	}

}
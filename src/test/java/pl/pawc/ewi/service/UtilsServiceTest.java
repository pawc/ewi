package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class UtilsServiceTest {

	@Autowired
	UtilsService utilsService;

	@Test
	public void testMyRound(){
		assertEquals(2.09, utilsService.myRound(1.7 * 1.23, true));
		assertEquals(2.1, utilsService.myRound(1.7 * 1.23, false));

		assertEquals(1.85, utilsService.myRound(1.5 * 1.23, true));
		assertEquals(1.8, utilsService.myRound(1.5 * 1.23, false));

		assertEquals(37.95, utilsService.myRound(3.3 * 11.5, true));
		assertEquals(38, utilsService.myRound(3.3 * 11.5, false));

	}

}
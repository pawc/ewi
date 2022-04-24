package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

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
		double a = 1.7;
		double b = 1.23;
		assertEquals(new BigDecimal("2.09"), utilsService.multiply(a, b, true));
		assertEquals(new BigDecimal("2.1"), utilsService.multiply(a, b, false));

		a = 1.5;
		b = 1.23;
		assertEquals(new BigDecimal("1.85"), utilsService.multiply(a, b, true));
		assertEquals(new BigDecimal("1.8"), utilsService.multiply(a, b, false));

		a = 3.3;
		b = 11.5;
		assertEquals(new BigDecimal("37.95"), utilsService.multiply(a, b, true));
		assertEquals(new BigDecimal("38.0"), utilsService.multiply(a, b, false));

	}

}
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
class ZuzycieServiceTest {

	@Autowired
	ZuzycieService zuzycieService;

	@Test
	@Transactional
	void testGetSuma() {

		assertEquals(1, zuzycieService.getSuma(1, 2022, 4));
		assertEquals(21, zuzycieService.getSuma(2, 2022, 4));
		assertEquals(0.2, zuzycieService.getSuma(26, 2022, 4));

	}

    /*private double myRound(double d){
		double r = (double) Math.round(d*100)/100;
		return (double) Math.round(r*10)/10;
	}*/

}
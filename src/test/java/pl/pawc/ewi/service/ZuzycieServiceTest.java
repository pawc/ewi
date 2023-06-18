package pl.pawc.ewi.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.NormaNotFoundException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class ZuzycieServiceTest {

	@Autowired
	ZuzycieService zuzycieService;

	@Test
	@Transactional
	void testGetSuma() {

		try {
			assertEquals(new BigDecimal("8.6"), zuzycieService.getSuma(1, 2022, 4, null));
			assertEquals(new BigDecimal("6.9"), zuzycieService.getSuma(2, 2022, 4, null));
			assertEquals(new BigDecimal("3.5"), zuzycieService.getSuma(3, 2022, 4, null));
			assertEquals(new BigDecimal("20.5"), zuzycieService.getSuma(4, 2022, 4, null));
			assertEquals(new BigDecimal("16.9"), zuzycieService.getSuma(1, 2022, 5, null));
			assertEquals(new BigDecimal("30.5"), zuzycieService.getSuma(2, 2022, 5, null));
			assertEquals(new BigDecimal("1.3"), zuzycieService.getSuma(3, 2022, 5, null));
			assertEquals(new BigDecimal("17.2"), zuzycieService.getSuma(4, 2022, 5, null));

		} catch (DocumentNotFoundException | NormaNotFoundException e) {
			fail();
		}

	}

	@Test
	@Transactional
	void testGetSumaYear(){

		assertEquals(new BigDecimal("35.19"), zuzycieService.getSumaYear(1, 2022));
		assertEquals(BigDecimal.ZERO, zuzycieService.getSumaYear(1, 2023));
		assertEquals(new BigDecimal("152.76"), zuzycieService.getSumaYear(2, 2022));
		assertEquals(BigDecimal.ZERO, zuzycieService.getSumaYear(2, 2023));

		assertEquals(BigDecimal.ZERO, zuzycieService.getSumaYear(26, 2023));

	}

}
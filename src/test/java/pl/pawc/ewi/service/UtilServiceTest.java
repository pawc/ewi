package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UtilServiceTest {

	@Autowired
	UtilService utilService;

	@Test
	void testFindAllActive() {
		BigDecimal[] calc = utilService.calc(new BigDecimal("132.7"), new BigDecimal("11.5"), new BigDecimal("1.5"),
				new BigDecimal("0"), new BigDecimal("0"), false);

		assertEquals(new BigDecimal("115.4"), calc[0]);
		assertEquals(new BigDecimal("17.3"), calc[1]);

		calc = utilService.calc(new BigDecimal("90"), new BigDecimal("10"), new BigDecimal("3.3"),
				new BigDecimal("2"), new BigDecimal("0"), false);

		assertEquals(new BigDecimal("55.0"), calc[0]);
		assertEquals(new BigDecimal("33.0"), calc[1]);

		calc = utilService.calc(new BigDecimal("1.23"), new BigDecimal("1.2"), new BigDecimal("4.2"),
				new BigDecimal("5.3"), new BigDecimal("0.7"), true);

		assertEquals(new BigDecimal("-8.41"), calc[0]);
		assertEquals(new BigDecimal("5.04"), calc[1]);
	}

}
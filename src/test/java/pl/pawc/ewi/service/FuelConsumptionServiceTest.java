package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.DocumentNotFoundException;
import pl.pawc.ewi.model.FuelConsumptionStandardNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class FuelConsumptionServiceTest {

	@Autowired
	FuelConsumptionService fuelConsumptionService;

	@Test
	@Transactional
	void testGetSuma() {

		try {
			assertEquals(new BigDecimal("8.6"), fuelConsumptionService.getSum(1, 2022, 4, null));
			assertEquals(new BigDecimal("6.9"), fuelConsumptionService.getSum(2, 2022, 4, null));
			assertEquals(new BigDecimal("3.5"), fuelConsumptionService.getSum(3, 2022, 4, null));
			assertEquals(new BigDecimal("20.5"), fuelConsumptionService.getSum(4, 2022, 4, null));
			assertEquals(new BigDecimal("16.9"), fuelConsumptionService.getSum(1, 2022, 5, null));
			assertEquals(new BigDecimal("30.5"), fuelConsumptionService.getSum(2, 2022, 5, null));
			assertEquals(new BigDecimal("1.3"), fuelConsumptionService.getSum(3, 2022, 5, null));
			assertEquals(new BigDecimal("17.2"), fuelConsumptionService.getSum(4, 2022, 5, null));

		} catch (DocumentNotFoundException | FuelConsumptionStandardNotFoundException e) {
			fail();
		}

	}

	@Test
	@Transactional
	void testGetSumaYear(){

		assertEquals(new BigDecimal("35.19"), fuelConsumptionService.getSumYear(1, 2022));
		assertEquals(BigDecimal.ZERO, fuelConsumptionService.getSumYear(1, 2023));
		assertEquals(new BigDecimal("152.76"), fuelConsumptionService.getSumYear(2, 2022));
		assertEquals(BigDecimal.ZERO, fuelConsumptionService.getSumYear(2, 2023));

		assertEquals(BigDecimal.ZERO, fuelConsumptionService.getSumYear(26, 2023));

	}

}
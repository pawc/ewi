package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.FuelInitialState;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.model.FuelInitialStateReport;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FuelInitialStateServiceTest {

	@Autowired
	FuelInitialStateService fuelInitialStateService;

	@Test
	@Transactional
	void test() {

		List<FuelInitialStateReport> stany = fuelInitialStateService.findBy(2022, 4);
		assertEquals(4, stany.size());
		stany.forEach(s -> {
			assertTrue(s.getFuelInitialStateId() != -1);
			assertTrue(s.getFuelInitialState().compareTo(BigDecimal.ZERO) > 0);
		});

		stany = fuelInitialStateService.findBy(2022, 5);
		assertEquals(4, stany.size());
		stany.forEach(s -> {
			assertEquals(-1, s.getFuelInitialStateId());
			assertEquals(BigDecimal.ZERO, s.getFuelInitialState());
		});

		FuelConsumptionStandard fuelConsumptionStandard = new FuelConsumptionStandard();
		fuelConsumptionStandard.setId(1);

		FuelInitialState fuelInitialState = new FuelInitialState();
		fuelInitialState.setValue(new BigDecimal("12.4"));
		fuelInitialState.setMonth(5);
		fuelInitialState.setFuelConsumptionStandard(fuelConsumptionStandard);
		fuelInitialState.setYear(2022);
		fuelInitialStateService.post(fuelInitialState);

		Optional<FuelInitialStateReport> first = fuelInitialStateService.findBy(2022, 5).stream()
				.filter(rs -> rs.getFuelConsumptionStandardId() == 1L).findFirst();
		assertNotNull(first.toString());
		assertTrue(first.isPresent());
		FuelInitialStateReport rs = first.get();
		assertNotNull(rs);
		assertEquals(new BigDecimal("12.4"), rs.getFuelInitialState());
		assertEquals("C1", rs.getMachineId());
		assertEquals("ON/H", rs.getUnit());

	}

}
package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.FuelConsumptionStandard;
import pl.pawc.ewi.entity.FuelInitialState;
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
	void postFuelInitialStateTest() {
		List<FuelInitialStateReport> initialStatesReport = fuelInitialStateService.findBy(2022, 4);
		assertEquals(4, initialStatesReport.size());
		initialStatesReport.forEach(s -> {
			assertTrue(s.getFuelInitialStateId() != -1);
			assertTrue(s.getFuelInitialState().compareTo(BigDecimal.ZERO) > 0);
		});

		initialStatesReport = fuelInitialStateService.findBy(2022, 5);
		assertEquals(4, initialStatesReport.size());
		initialStatesReport.forEach(s -> {
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

	@Test
	@Transactional
	void postFuelInitialStatesTest(){
		FuelConsumptionStandard standard1 = new FuelConsumptionStandard();
		standard1.setId(1);

		FuelConsumptionStandard standard2 = new FuelConsumptionStandard();
		standard2.setId(2);

		FuelInitialState initialState1 = new FuelInitialState();
		initialState1.setFuelConsumptionStandard(standard1);
		initialState1.setYear(2024);
		initialState1.setMonth(6);
		initialState1.setValue(BigDecimal.valueOf(3.45));

		FuelInitialState initialState2 = new FuelInitialState();
		initialState2.setFuelConsumptionStandard(standard2);
		initialState2.setYear(2024);
		initialState2.setMonth(6);
		initialState2.setValue(BigDecimal.valueOf(3.46));

		List<FuelInitialState> initialStates = List.of(initialState1, initialState2);

		List<FuelInitialStateReport> initialStatesBefore = fuelInitialStateService.findBy(2024, 6);
		initialStatesBefore.forEach(state -> {
			if(state.getFuelConsumptionStandardId() == 1){
				assertEquals(BigDecimal.ZERO, state.getFuelInitialState());
			}
			if(state.getFuelConsumptionStandardId() == 2){
				assertEquals(BigDecimal.ZERO, state.getFuelInitialState());
			}
		});

		fuelInitialStateService.postFuelInitialStates(initialStates);

		List<FuelInitialStateReport> initialStatesAfter = fuelInitialStateService.findBy(2024, 6);
		initialStatesAfter.forEach(state -> {
			if(state.getFuelConsumptionStandardId() == 1){
				assertEquals(new BigDecimal("3.45"), state.getFuelInitialState());
			}
			if(state.getFuelConsumptionStandardId() == 2){
				assertEquals(new BigDecimal("3.46"), state.getFuelInitialState());
			}
		});
	}

}
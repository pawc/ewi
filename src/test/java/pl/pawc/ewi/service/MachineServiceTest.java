package pl.pawc.ewi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Machine;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MachineServiceTest {

	@Autowired
	MachineService machineService;

	@Test
	@Transactional
	void testFindAllActive() {

		assertEquals(2, machineService.findAllActive().size());

	}

	@Test
	@Transactional
	void testFindAllUncategorized() {

		assertEquals(1, machineService.findAllUncategorized().size());

	}

	@Test
	@Transactional
	void testPost() throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		String input = "{\"id\":\"ABC123\",\"name\":\"Machine 1\",\"description\":\"test machine\",\"fuelConsumptionStandards\":[{\"value\":\"1.2\",\"unit\":\"L/H\",\"usedForHeating\":false},{\"value\":\"3.27\",\"unit\":\"ON/H\",\"usedForHeating\":true}],\"categories\":[],\"active\":true}";
		Machine machine = objectMapper.readValue(input, Machine.class);

		Machine m = machineService.post(machine);

		assertEquals(2, m.getFuelConsumptionStandards().size());
		assertTrue(m.isActive());
		m.getFuelConsumptionStandards().forEach(n -> assertEquals("ABC123", n.getMachine().getId()));

	}

	@Test
	@Transactional
	void testGetPut() {

		Machine c1 = machineService.get("C1", "2022-05");
		assertNotNull(c1);
		assertNotNull(c1.getSumOfKilometers());
		c1.getFuelConsumptionStandards().forEach(n -> assertNotNull(n.getSum()));
		assertEquals("Ciagnik 1", c1.getName());
		c1.setName("Ciągnik Test");
		machineService.put(c1);
		c1 = machineService.get("C1", "2022-05");
		assertNotNull(c1);
		assertEquals("Ciągnik Test", c1.getName());

	}

}
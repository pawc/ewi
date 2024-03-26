package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class KilometersServiceTest {

	@Autowired
	KilometersService kilometersService;

	@Autowired
	CategoryService categoryService;

	@Test
	@Transactional
	void testPostKilometers() {
		Kilometers km = new Kilometers();
		Machine machine = new Machine();
		machine.setId("C1");
		km.setMachine(machine);
		km.setYear(2024);
		km.setMonth(1);
		km.setValue(BigDecimal.valueOf(1.23));

		kilometersService.post(List.of(km));
        assertNotNull(categoryService.findAll().get(0).getMachines());

		km.setValue(BigDecimal.valueOf(1.23));
		kilometersService.post(List.of(km));
		assertNotNull(categoryService.findAll().get(0).getMachines());

	}

}
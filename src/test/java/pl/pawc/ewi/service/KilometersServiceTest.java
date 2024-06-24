package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Kilometers;
import pl.pawc.ewi.entity.Machine;
import jakarta.transaction.Transactional;
import pl.pawc.ewi.repository.KilometersRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class KilometersServiceTest {

	@Autowired
	KilometersService kilometersService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	KilometersRepository kilometersRepository;

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
	@Test
	@Transactional
	void testKilometersRepository(){
		Machine machine = new Machine();
		machine.setId("C1");
		Optional<Kilometers> oneByMachineAndYearAndMonth =
				kilometersRepository.findOneByMachineAndYearAndMonth(machine, 2026, 1);

		BigDecimal val = oneByMachineAndYearAndMonth.map(Kilometers::getValue).orElse(BigDecimal.ZERO);
		assertEquals(BigDecimal.ZERO, val);
	}

}
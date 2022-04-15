package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.RaportStan;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class StanServiceTest {

	@Autowired
	StanService stanService;

	@Test
	@Transactional
	void testFindBy() {

		List<RaportStan> stany = stanService.findBy(2022, 4);
		assertEquals(3, stany.size());
		stany.forEach(s -> {
			assertTrue(s.getStanid() != -1);
			assertTrue(s.getStanpoczatkowy() > 0);
		});

		stany = stanService.findBy(2022, 5);
		assertEquals(3, stany.size());
		stany.forEach(s -> {
			assertEquals(-1, s.getStanid());
			assertEquals(0, s.getStanpoczatkowy());
		});
	}

}
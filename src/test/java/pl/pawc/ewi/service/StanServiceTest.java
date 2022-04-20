package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.model.RaportStan;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class StanServiceTest {

	@Autowired
	StanService stanService;

	@Test
	@Transactional
	void test() {

		List<RaportStan> stany = stanService.findBy(2022, 4);
		assertEquals(4, stany.size());
		stany.forEach(s -> {
			assertTrue(s.getStanid() != -1);
			assertTrue(s.getStanpoczatkowy() > 0);
		});

		stany = stanService.findBy(2022, 5);
		assertEquals(4, stany.size());
		stany.forEach(s -> {
			assertEquals(-1, s.getStanid());
			assertEquals(0, s.getStanpoczatkowy());
		});

		Norma norma = new Norma();
		norma.setId(1);

		Stan stan = new Stan();
		stan.setWartosc(12.4);
		stan.setMiesiac(5);
		stan.setNorma(norma);
		stan.setRok(2022);
		stanService.post(stan);

		Optional<RaportStan> first = stanService.findBy(2022, 5).stream()
				.filter(rs -> rs.getNormaid() == 1L).findFirst();
		assertTrue(first.isPresent());
		RaportStan rs = first.get();
		assertNotNull(rs);
		assertEquals(12.4, rs.getStanpoczatkowy());
		assertEquals("C1", rs.getMaszynaid());
		assertEquals("ON/H", rs.getJednostka());

	}

}
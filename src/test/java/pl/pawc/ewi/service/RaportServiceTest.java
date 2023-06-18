package pl.pawc.ewi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.model.Raport;
import pl.pawc.ewi.model.RaportKilometry;
import pl.pawc.ewi.model.RaportRoczny;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RaportServiceTest {

	@Autowired
	RaportService raportService;

	@Test
	@Transactional
	void testGetKilometryRaport() {

		List<RaportKilometry> kilometryRaport = raportService.getKilometryRaport(2022, 4);
		assertEquals(2, kilometryRaport.size());

		kilometryRaport = raportService.getKilometryRaport(2022, 5);
		assertEquals(2, kilometryRaport.size());

		for (RaportKilometry r : kilometryRaport) {
			assertEquals(BigDecimal.ZERO, r.getStanpoczatkowy());
			assertNotNull(r.getMaszynaid());
			assertNotNull(r.getMaszynanazwa());
			assertNotNull(r.toString());
		}

	}

	@Test
	@Transactional
	void testGetRaportRoczny() {

		List<RaportRoczny> raportRoczny = raportService.getRaportRoczny(2022);
		assertEquals(2, raportRoczny.size());
		assertNotNull(raportRoczny.stream().findFirst().toString());

		RaportRoczny rr = raportRoczny.stream().filter(r -> "L/H".equals(r.getJednostka())).findFirst().orElseThrow();
		assertEquals(new BigDecimal("152.76"), rr.getSuma());
		assertEquals(new BigDecimal("152.76"), rr.getSumaRazyWaga());

		assertEquals(new BigDecimal("35.19"), raportRoczny.stream().filter(r -> "ON/H".equals(r.getJednostka())).findFirst().get().getSuma());

		assertEquals(0, raportService.getRaportRoczny(2023).size());

	}

	@Test
	@Transactional
	void testGetRaport() {

		List<Raport> monthlyReport202204 = raportService.getRaport(2022, 4, false);
		assertEquals(4, monthlyReport202204.size());
		Raport raport = monthlyReport202204.stream().filter(r -> "C1-1".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(8.6), raport.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(102.9), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(121.2), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(19.1), raport.getSuma());
		assertEquals(BigDecimal.valueOf(44.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(18.7), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(15.5), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(222.9), raport.getEndStateKilometry());

		raport = monthlyReport202204.stream().filter(r -> "C1-2".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(6.9), raport.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(102.9), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(121.2), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(116.3), raport.getSuma());
		assertEquals(BigDecimal.valueOf(122.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(25.5), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(222.9), raport.getEndStateKilometry());

		raport = monthlyReport202204.stream().filter(r -> "W2-3".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(3.5), raport.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(91.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(68.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(42.0), raport.getSuma());
		assertEquals(BigDecimal.valueOf(65.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(25.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(10.0), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(141.0), raport.getEndStateKilometry());

		raport = monthlyReport202204.stream().filter(r -> "W2-4".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(20.5), raport.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(91.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(68.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(20.6), raport.getSuma());
		assertEquals(BigDecimal.valueOf(29.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(17.1), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(141.0), raport.getEndStateKilometry());

		List<Raport> monthlyReport202205 = raportService.getRaport(2022, 5, false);
		assertEquals(4, monthlyReport202205.size());

		raport = monthlyReport202205.stream().filter(r -> "C1-1".equals(r.getMaszynaidnormaid())).findAny().get();
		assertEquals(BigDecimal.valueOf(16.9), raport.getEndState());
		assertEquals(BigDecimal.valueOf(0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(58.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(41.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(16.1), raport.getSuma());
		assertEquals(BigDecimal.valueOf(57.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(24.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(13.1), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(58.0), raport.getEndStateKilometry());

		raport = monthlyReport202205.stream().filter(r -> "C1-2".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(30.5), raport.getEndState());
		assertEquals(BigDecimal.valueOf(0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(58.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(41.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(36.5), raport.getSuma());
		assertEquals(BigDecimal.valueOf(67.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(8.0), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(58.0), raport.getEndStateKilometry());

		raport =  monthlyReport202205.stream().filter(r -> "W2-3".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(1.3),raport.getEndState());
		assertEquals(BigDecimal.valueOf(0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(61.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(48.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(70.7), raport.getSuma());
		assertEquals(BigDecimal.valueOf(86.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(14.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(16.8), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(61.0), raport.getEndStateKilometry());

		raport = monthlyReport202205.stream().filter(r -> "W2-4".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(17.2),raport.getEndState());
		assertEquals(BigDecimal.valueOf(0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(61.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(48.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(27.8), raport.getSuma());
		assertEquals(BigDecimal.valueOf(45.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(23.1), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(61.0), raport.getEndStateKilometry());

		assertEquals(0, raportService.getRaport(2023, 5, false).size());

		List<Raport> q2Report2022 = raportService.getRaport(2022, 2, true);
		assertEquals(4, q2Report2022.size());

		raport = q2Report2022.stream().filter(r -> "C1-1".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(25.5), raport.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(160.9), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(162.2), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(35.2), raport.getSuma());
		assertEquals(BigDecimal.valueOf(101.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(42.7), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(28.6), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(280.9), raport.getEndStateKilometry());

		raport = q2Report2022.stream().filter(r -> "C1-2".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(37.4), raport.getEndState());
		assertEquals(BigDecimal.valueOf(120.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(160.9), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(162.2), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(152.8), raport.getSuma());
		assertEquals(BigDecimal.valueOf(189.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(33.5), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(280.9), raport.getEndStateKilometry());

		raport = q2Report2022.stream().filter(r -> "W2-3".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(4.8), raport.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(152.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(116.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(112.7), raport.getSuma());
		assertEquals(BigDecimal.valueOf(151.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(39.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(26.8), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(202.0), raport.getEndStateKilometry());

		raport = q2Report2022.stream().filter(r -> "W2-4".equals(r.getMaszynaidnormaid())).findAny().orElseThrow();
		assertEquals(BigDecimal.valueOf(37.7), raport.getEndState());
		assertEquals(BigDecimal.valueOf(50.0), raport.getStankilometry());
		assertEquals(BigDecimal.valueOf(152.0), raport.getKilometry());
		assertEquals(BigDecimal.valueOf(116.0), raport.getKilometryprzyczepa());
		assertEquals(BigDecimal.valueOf(48.4), raport.getSuma());
		assertEquals(BigDecimal.valueOf(74.0), raport.getZatankowano());
		assertEquals(BigDecimal.valueOf(0.0), raport.getOgrzewanie());
		assertEquals(BigDecimal.valueOf(40.2), raport.getSumagodzin());
		assertEquals(BigDecimal.valueOf(202.0), raport.getEndStateKilometry());

		assertEquals(0, raportService.getRaport(2022, 1, true).size());

	}

}
package pl.pawc.ewi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pawc.ewi.entity.Norma;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.NormaRepository;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ZuzycieServiceTest {

	@Autowired
	DokumentRepository dokumentRepository;

	@Autowired
	NormaRepository normaRepository;

	@Autowired
	ZuzycieService zuzycieService;

	private static final Logger logger = LogManager.getLogger(ZuzycieServiceTest.class);

	@Test
	@Transactional
	void testA() {

		Iterable<Norma> all = normaRepository.findAll();
		for(Norma norma : all){
			for(int year = 2021; year <= 2022; year++){
				for(int month = 1; month <= 12; month++){
					logger.info("Norma_id {} {}-{}", norma.getId(), year,month);
					double s1 = dokumentRepository.getSuma(norma.getId(), year, month);
					double s2 = zuzycieService.getSuma(norma.getId(), year, month);

					assertEquals(myRound(s1), s2);
				}
			}
		}

	}

	private double myRound(double d){
		double r = (double) Math.round(d*100)/100;
		return (double) Math.round(r*10)/10;
	}

}
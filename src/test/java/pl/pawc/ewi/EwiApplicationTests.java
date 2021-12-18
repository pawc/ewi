package pl.pawc.ewi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawc.ewi.entity.Stan;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.NormaRepository;
import pl.pawc.ewi.repository.StanRepository;
import pl.pawc.ewi.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
class EwiApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DokumentRepository dokumentRepository;

	@Autowired
	StanRepository stanRepository;

	@Autowired
	NormaRepository normaRepository;

	@Test
	@Transactional
	void contextLoads() {

	}

}
package pl.pawc.ewi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawc.ewi.repository.DokumentRepository;
import pl.pawc.ewi.repository.UserRepository;

@SpringBootTest
class EwiApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DokumentRepository dokumentRepository;

	@Test
	void contextLoads() {

	}

}
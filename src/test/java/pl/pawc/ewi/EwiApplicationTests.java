package pl.pawc.ewi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

@SpringBootTest
class EwiApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {

		String password = passwordEncoder.encode("admin");
		User user = new User();
		user.setLogin("admin");
		user.setPassword(password);
		userRepository.save(user);

	}

}
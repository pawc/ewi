package pl.pawc.ewi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class EwiApplication {

	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(EwiApplication.class, args);
	}

}
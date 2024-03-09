package pl.pawc.ewi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class EwiApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/paliwo");
		SpringApplication.run(EwiApplication.class, args);
	}

}
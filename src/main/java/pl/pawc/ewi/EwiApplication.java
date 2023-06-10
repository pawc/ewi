package pl.pawc.ewi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawc.ewi.config.auth.DatabaseUserDetailsPasswordService;
import pl.pawc.ewi.config.auth.DatabaseUserDetailsService;

@RequiredArgsConstructor
@SpringBootApplication
public class EwiApplication {

	@Value("${bcryptWorkFactor}")
	String bcryptWorkFactor;

	private final DatabaseUserDetailsService databaseUserDetailsService;
	private final DatabaseUserDetailsPasswordService databaseUserDetailsPasswordService;

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/paliwo");
		SpringApplication.run(EwiApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		int strength;
		try{
			strength = Integer.parseInt(bcryptWorkFactor);
		}
		catch(NumberFormatException e){
			strength = 10;
		}
		return new BCryptPasswordEncoder(strength);
	}

	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {

		DaoAuthenticationProvider provider =
				new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(databaseUserDetailsService);
		provider.setUserDetailsPasswordService(databaseUserDetailsPasswordService);

		return provider;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
		return authenticationManagerBuilder.build();
	}

}
package pl.pawc.ewi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pawc.ewi.config.auth.DatabaseUserDetailsPasswordService;
import pl.pawc.ewi.config.auth.DatabaseUserDetailsService;

@RequiredArgsConstructor
@SpringBootApplication
@EnableWebSecurity
public class EwiApplication extends WebSecurityConfigurerAdapter {

	private final Environment env;

	@Value("${bcryptWorkFactor}")
	String bcryptWorkFactor;

	private final DatabaseUserDetailsService databaseUserDetailsService;
	private final DatabaseUserDetailsPasswordService databaseUserDetailsPasswordService;

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/paliwo");
		SpringApplication.run(EwiApplication.class, args);
	}

	@Override
	public void configure(WebSecurity web){
		web
			.ignoring()
			.antMatchers("/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
			.antMatchers("/login*").permitAll()
			.antMatchers("/*").access("hasRole('ROLE_USER')")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/", true)
			.failureUrl("/login?error=true")
			.and()
			.logout()
			.logoutUrl("/logout")
			.deleteCookies("JSESSIONID");

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

	@Override
	protected void configure(AuthenticationManagerBuilder auth){
		auth.authenticationProvider(daoAuthenticationProvider());
	}

}
package pl.pawc.ewi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableWebSecurity
public class EwiApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(EwiApplication.class, args);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
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

}
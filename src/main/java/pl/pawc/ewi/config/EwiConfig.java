package pl.pawc.ewi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EwiConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/home")
                    .permitAll()
                    .requestMatchers("/css/**")
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(login ->
                    login.loginPage("/home")
            )
            .build();
    }

}
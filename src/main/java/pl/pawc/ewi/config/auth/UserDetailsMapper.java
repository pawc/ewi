package pl.pawc.ewi.config.auth;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.User;

@Component
public class UserDetailsMapper {

    UserDetails toUserDetails(User user){

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getLogin())
            .password(user.getPassword())
            .roles("USER")
            .build();

    }

}
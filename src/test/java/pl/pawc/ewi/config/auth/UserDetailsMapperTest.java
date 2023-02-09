package pl.pawc.ewi.config.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pawc.ewi.entity.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserDetailsMapperTest {

    @InjectMocks
    UserDetailsMapper mapper;

    @Test
    void toUserDetailsTest() {

        User user = new User();
        user.setLogin("john");
        user.setPassword("abc123");
        UserDetails userDetails = mapper.toUserDetails(user);
        assertNotNull(userDetails);

    }

}
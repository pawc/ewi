package pl.pawc.ewi.config.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import pl.pawc.ewi.entity.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1",
})
class UserDetailsMapperTest {

    @Test
    void toUserDetailsTest() {

        UserDetailsMapper mapper = new UserDetailsMapper();
        User user = new User();
        user.setLogin("john");
        user.setPassword("abc123");
        UserDetails userDetails = mapper.toUserDetails(user);
        assertNotNull(userDetails);

    }

}
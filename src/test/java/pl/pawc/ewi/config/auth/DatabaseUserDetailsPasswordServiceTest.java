package pl.pawc.ewi.config.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsPasswordServiceTest {

    @InjectMocks
    DatabaseUserDetailsPasswordService service;

    @Mock
    UserDetailsMapper userDetailsMapper;

    @Mock
    UserRepository userRepository;

    @Test
    void updatePasswordTest(){

        User user = new User("pawc", "abc123", Collections.emptyList());
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(new pl.pawc.ewi.entity.User()));
        service.updatePassword(user, "123abc");
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        Mockito.verify(userDetailsMapper, Mockito.times(1)).toUserDetails(any());

    }

}
package pl.pawc.ewi.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitialDataLoaderTest {

    @InjectMocks
    InitialDataLoader initialDataLoader;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    ApplicationArguments applicationArguments;

    @Test
    void myUserTest() {

        Optional<User> optionalUser = Optional.empty();
        when(userRepository.findById(anyString())).thenReturn(optionalUser);
        ReflectionTestUtils.setField(initialDataLoader, "myUser", "pawc");
        ReflectionTestUtils.setField(initialDataLoader, "myPassword", "abc123");
        assertNotNull(initialDataLoader);
        initialDataLoader.run(applicationArguments);

        ReflectionTestUtils.setField(initialDataLoader, "myUser", "pawc123");
        initialDataLoader.run(applicationArguments);

        verify(userRepository, times(2)).save(any());
        verify(userRepository, times(2)).findById(any());
        verify(passwordEncoder, times(2)).encode(anyString());

    }

}
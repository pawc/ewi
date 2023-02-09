package pl.pawc.ewi.config.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @InjectMocks
    DatabaseUserDetailsService databaseUserDetailsService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserDetailsMapper userDetailsMapper;

    @Test
    void loadUserByUsernameTest(){

        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        databaseUserDetailsService.loadUserByUsername("pawc");
        verify(userRepository, times(1)).findById(eq("pawc"));
        verify(userDetailsMapper, times(1)).toUserDetails(any());

    }

}
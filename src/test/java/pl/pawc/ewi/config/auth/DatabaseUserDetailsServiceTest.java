package pl.pawc.ewi.config.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserDetailsMapper userDetailsMapper;

    @Test
    void loadUserByUsernameTest(){

        DatabaseUserDetailsService databaseUserDetailsService = new DatabaseUserDetailsService(userRepository, userDetailsMapper);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        databaseUserDetailsService.loadUserByUsername("pawc");
        verify(userRepository, times(1)).findById(eq("pawc"));
        verify(userDetailsMapper, times(1)).toUserDetails(any());

    }

}
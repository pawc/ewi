package pl.pawc.ewi.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> byId = userRepository.findById(username);
        if(!byId.isPresent()) throw new UsernameNotFoundException(username);

        User user = byId.get();
        return userDetailsMapper.toUserDetails(user);

    }

}
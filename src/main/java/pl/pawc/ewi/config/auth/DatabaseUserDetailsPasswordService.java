package pl.pawc.ewi.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DatabaseUserDetailsPasswordService implements UserDetailsPasswordService {

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {

        Optional<User> byId = userRepository.findById(user.getUsername());
        if(!byId.isPresent()) throw new UsernameNotFoundException(user.getUsername());

        User u = byId.get();
        u.setPassword(newPassword);

        userRepository.save(u);

        return userDetailsMapper.toUserDetails(u);

    }

}
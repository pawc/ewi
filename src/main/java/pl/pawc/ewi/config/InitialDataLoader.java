package pl.pawc.ewi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.pawc.ewi.entity.User;
import pl.pawc.ewi.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class InitialDataLoader implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${myUser}")
    private String myUser;

    @Value("${myPassword}")
    private String myPassword;

    @Override
    public void run(ApplicationArguments args){

        myUser();

    }

    private void myUser() {
        User user = new User();
        if(myUser.length() > 4 && myPassword.length() > 4 && !userRepository.findById(myUser).isPresent()){
            user.setLogin(myUser);
            user.setPassword(passwordEncoder.encode(myPassword));
            userRepository.save(user);
        }
        else {
            if(!userRepository.findById(myUser).isPresent()){
                user.setLogin("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                userRepository.save(user);
            }
        }
    }

}
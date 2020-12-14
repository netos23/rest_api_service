package ru.fbtw.navigator.rest_api_service.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.Role;
import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.repository.UserRepo;


import java.util.Collections;

@Service
public class UserService implements UserDetailsService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUserByUsername(username);
    }

    public void addUser(User user) {
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = loadUserByUsername(login);
        if(user != null){
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}

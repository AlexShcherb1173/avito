package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserServiceConfig;
import ru.skypro.homework.service.map.UserMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserServiceConfig userServiceConfig;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMap userMap;

    public AuthServiceImpl(UserServiceConfig userServiceConfig,
                           PasswordEncoder encoder,
                           UserRepository userRepository,
                           UserMap userMap) {
        this.userServiceConfig = userServiceConfig;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.userMap = userMap;
    }

    @Override
    public boolean login(String userName, String password) {
        if (userServiceConfig.findByUsername(userName).isEmpty()) {
            return false;
        }
        UserDetails userDetails = userServiceConfig.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (userServiceConfig.findByUsername(register.getUsername()).isPresent()) {
            return false;
        }
        register.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(userMap.registerToUser(register));
        return true;
    }

}

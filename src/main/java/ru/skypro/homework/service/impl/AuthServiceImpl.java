package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.filter.SecurityUserService;
import ru.skypro.homework.service.AuthService;


@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final PasswordEncoder encoder;
    private final SecurityUserService manager;


    public AuthServiceImpl(PasswordEncoder encoder, SecurityUserService manager) {
        this.encoder = encoder;
        this.manager = manager;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            logger.info("AuthService login function if user not exists");
            return false;
        }

        UserDetails userDetails = manager.loadUserByUsername(userName);
        logger.info("Password in login: {}, Password in DB: {}", password, userDetails.getPassword());
        return encoder.matches(password, userDetails.getPassword());

    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        logger.info("Register password :{}", register.getPassword());
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build(),register.getFirstName(), register.getLastName(), register.getPhone());

        return true;
    }
}



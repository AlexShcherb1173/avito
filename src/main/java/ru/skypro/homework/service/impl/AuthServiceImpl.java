package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserDetailsService manager,
                           PasswordEncoder passwordEncoder, UserService userService) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public boolean login(Login login) {
        UserDetails userDetails;
        try {
            userDetails = manager.loadUserByUsername(login.getUsername());
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        logger.info("Invoked method register({})", register.getUsername());
        try {
            register.setPassword(encoder.encode(register.getPassword()));
            userService.createUser(register);
        } catch (UserAlreadyExistException e) {
            logger.info("Register failed, user({}) already exists.", register.getUsername());
            return false;
        }
        return true;
    }
}

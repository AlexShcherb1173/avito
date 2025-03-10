package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserAlreadyExistsException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder,
                           UserServiceImpl userService) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            if (!manager.userExists(userName)) {
                throw new UsernameNotFoundException("Пользователь не зарегистрирован");
            }
            UserDetails userDetails = manager.loadUserByUsername(userName);
            if (!encoder.matches(password, userDetails.getPassword())) {
                throw new WrongPasswordException("Пароль неверный");
            }
            return true;
        } catch (UsernameNotFoundException | WrongPasswordException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        try {
            userService.createUser(register);
            return true;
        } catch (UserAlreadyExistsException e) {
            log.error(e.getMessage());
        }
        return false;
    }

}

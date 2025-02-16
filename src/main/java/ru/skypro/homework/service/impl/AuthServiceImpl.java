package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.exceptions.UserAlreadyExistsException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final UserServiceImpl userService;
    private final AvitoUserDetailsService avitoUserDetailsService;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserServiceImpl userService, AvitoUserDetailsService avitoUserDetailsService) {
        this.encoder = passwordEncoder;
        this.userService = userService;
        this.avitoUserDetailsService = avitoUserDetailsService;
    }

    @Override
    public boolean login(LoginDTO loginDTO) {
        try {
            UserDetails userDetails = avitoUserDetailsService.loadUserByUsername(loginDTO.getUsername());
            if (!encoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                throw new WrongPasswordException("Пароль неверный");
            }
            return true;
        } catch (WrongPasswordException | UsernameNotFoundException e) {
            log.error("Ошибка аутентификации: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long registerId(RegisterDTO registerDTO) {
        try {
            log.info("User created: {}", registerDTO.getUsername());
            return userService.createUser(registerDTO);
        } catch (UserAlreadyExistsException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}

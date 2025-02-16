package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.exceptions.UserAlreadyExistsException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final AvitoUserDetailsService avitoUserDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           AvitoUserDetailsService avitoUserDetailsService, UserRepository userRepository, UserMapper userMapper, UserMapper userMapper1) {
        this.encoder = passwordEncoder;
        this.avitoUserDetailsService = avitoUserDetailsService;
        this.userRepository = userRepository;
        this.userMapper = userMapper1;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = avitoUserDetailsService.loadUserByUsername(userName);
            if (!encoder.matches(password, userDetails.getPassword())) {
                throw new WrongPasswordException("Пароль неверный");
            }
            log.info("Выполнен вход {}", userName);
            return true;
        } catch (WrongPasswordException | UsernameNotFoundException e) {
            log.error("Ошибка аутентификации: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean register(RegisterDTO register) {
        if (register.getUsername() != null &&
                userRepository.findByUsername(register.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем " + register.getUsername() + " уже существует");
        }
        userRepository.save(userMapper.registerToUser(register));
        log.info("Зарегистрирован пользователь {}", register.getUsername());
        return true;
    }
}
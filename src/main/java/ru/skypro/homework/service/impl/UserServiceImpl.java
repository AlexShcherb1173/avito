package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           JdbcUserDetailsManager userDetailsManager,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUserEntity(Register register) {
        log.info("Вы вошли в метод saveUserEntity");
        if (register == null) { // Проверяем, что регистрация не null
            log.error("Переданный объект Register is null");
            throw new NotFoundException("Такого пользователя нет");
        }
        if (userRepository.existsByUsername(register.getUsername())) { // Проверяем наличие пользователя с таким же email
            log.error("Пользователь с таким email уже существует: {}", register.getUsername());
            throw new NotFoundException("Пользователь с таким email уже существует");
        }
        String encodedPassword = passwordEncoder.encode(register.getPassword());
        register.setPassword(encodedPassword); // Хешируем пароль перед сохранением
        org.springframework.security.core.userdetails.UserDetails userDetails = //Создаем пользователя для JdbcUserDetailsManager
                org.springframework.security.core.userdetails.User.withUsername(register.getUsername())
                        .password(encodedPassword)
                        .roles(Role.USER.name())
                        .accountLocked(false)
                        .disabled(false)
                        .build();
        userDetailsManager.createUser(userDetails); // Создаем пользователя в JdbcUserDetailsManager
        userRepository.save(userMapper.toUserEntity(register)); // Сохраняем пользователя в базе данных
        log.info("Пользователь успешно сохранен: {}", register.getUsername());
    }

    public User getUserDto() {
        User user = new User();
        return user;
    }
}

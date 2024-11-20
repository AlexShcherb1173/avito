package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ImageServiceImpl imageService;
    private final AuthenticationServiceImpl authenticationService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           JdbcUserDetailsManager userDetailsManager,
                           PasswordEncoder passwordEncoder,
                           ImageServiceImpl imageService,
                           AuthenticationServiceImpl authenticationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.authenticationService = authenticationService;
    }

    public void saveUserEntity(Register register) {
        log.info("Вошли в метод saveUserEntity");
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

    public void updatePassword(NewPassword newPassword, String username) {
        log.info("Вошли в метод setPassword сервиса UserServiceImpl\nПриняты данные:" +
                        "старый пароль {} | новый пароль {} | имя пользователя {}",
        newPassword.getCurrentPassword(), newPassword.getNewPassword(), username);

        String oldPasswordFromDb = userRepository.findPasswordByUsername(username);
        log.info("Получен хешированный пароль из БД по имени пользователя.\n" +
                "Хешированный пароль из БД: {}", oldPasswordFromDb);

        if (passwordEncoder.matches(newPassword.getCurrentPassword(), oldPasswordFromDb) && // Сравниваем старый со старым из бд
                (!passwordEncoder.matches(newPassword.getNewPassword(), oldPasswordFromDb)) && // Сравниваем новый со старым из бд
                (newPassword.getNewPassword().length() >= 8)) { // Проверяем длину пароля, не менее 8 символов
            log.info("Требования для смены пароля выполнены ");
            userRepository.changePassword(passwordEncoder.encode(newPassword.getNewPassword()), username);
            log.info("Пароль был успешно изменен");
        } else {
            log.info("Требования для смены пароля не выполнены");
            throw new SecurityException("Пароль не соответствует требованиям");
        }
    }

    public User getCurrentUser() {
        String username = authenticationService.getAuthenticatedUsername();
        if (username == null) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        UserModel userEntity = userRepository.findByUsername(username);

        // Преобразуем сущность пользователя в модель
        return userMapper.toModel(userEntity);
    }

    public void updateUser(User updateUser, String username) {
        log.info("Вошли в метод updateUser сервиса UserServiceImpl " +
                " получен объект: {} ", updateUser);
        UserModel userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UnauthorizedException("Пользователь не найден");
        }
        userEntity.setFirstName(updateUser .getFirstName());
        userEntity.setLastName(updateUser .getLastName());
        userEntity.setPhone(updateUser .getPhone());
        userRepository.save(userEntity);
        log.info("Смена данных пользователя успешно завершена");
    }

    public void updateUserImage(UserModel user, MultipartFile image) throws IOException {
        log.info("Вошли в метод updateUserImage сервиса UserServiceImpl " + " полечен объект: {}", image.getOriginalFilename());
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }
        imageService.saveImage(image, user.getUsername());
        log.info("Изображение для пользователя: {}, успешно обновлено", user.getUsername());
    }
}

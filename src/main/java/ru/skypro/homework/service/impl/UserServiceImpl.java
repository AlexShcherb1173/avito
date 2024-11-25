package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageServiceImpl imageService;

    @Value("avatar")
    private String avatarDir;

    public void updatePassword(NewPassword newPassword, String username) {
        log.info("Вошли в метод updatePassword сервиса UserServiceImpl. Приняты данные:" +
                        "старый пароль {} | новый пароль {} | имя пользователя {}",
        newPassword.getCurrentPassword(), newPassword.getNewPassword(), username);

        String oldPasswordFromDb = userRepository.findPasswordByUsername(username);
        log.info("Получен хешированный пароль из БД по имени пользователя." +
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

    @Override
    public void getUser(UserEntity user) {

    }

    @Override
    public void updateUser(UpdateUser updateUser, String username) {
        log.info("Вошли в метод updateUser сервиса UserServiceImpl получен объект: {}", updateUser);
        userRepository.changeUserData(updateUser.getFirstName(),
                updateUser.getLastName(),
                updateUser.getPhone(),
                username);
        log.info("Смена данных пользователя успешно завершена");
    }

    @Override
    public void updateUserImage(MultipartFile image, String username) throws IOException {
        imageService.saveImageToDisk(image, username);
    }
}

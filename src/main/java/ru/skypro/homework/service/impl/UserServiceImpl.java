package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void updatePassword(NewPassword newPassword, String username) {
        log.info("Вошли в метод setPassword сервиса UserServiceImpl. Приняты данные:" +
                        "старый пароль {} | новый пароль {} | имя пользователя {}",
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword(),
                username);
        String oldPasswordFromDb = userRepository.findPasswordByUsername(username);
        log.info("Получен хешированный пароль из БД по имени пользователя. " +
                "Хешированный пароль из БД: {}", oldPasswordFromDb);
        if (passwordEncoder.matches(newPassword.getCurrentPassword(), oldPasswordFromDb) &&
                (!passwordEncoder.matches(newPassword.getNewPassword(), oldPasswordFromDb)) &&
                (newPassword.getNewPassword().length() >= 8))  {
            log.info("Требования для смены пароля выполнены");
            userRepository.changePassword(passwordEncoder.encode(newPassword.getNewPassword()), username);
            log.info("Пароль был успешно изменён");
        } else {
            throw new SecurityException("Пароль не соответствует требованиям");
        }
    }

    @Override
    public User getUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        User userDto = userMapper.toUserDto(userEntity);
        return userDto;
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
    public void updateUserAvatar(MultipartFile avatarImage, String username) throws IOException {
        log.info("Вошли в метод updateUserAvatar сервиса UserServiceImpl получено изображение: {}",
                avatarImage.getOriginalFilename());
        UUID uuid = UUID.randomUUID();
        String filePathString = "/avatar/" + uuid + "." + getExtension(avatarImage);
        Path filePath = Path.of("avatar", uuid + "." + getExtension(avatarImage));

        Files.createDirectories((filePath.getParent()));

        try (InputStream is = avatarImage.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            log.info("Файл успешно сохранён на диск. Полное имя файла: {}", filePathString);
        }
        userRepository.saveAvatarPath(filePathString, username);
        log.info("Путь картинки сохранён в столбец image, таблицы app_user");
    }

    @Override
    public byte[] findAvatarImageByFilename(String fileName) throws IOException {
        log.info("Вошли в метод findAvatarImageByFilename сервиса UserServiceImpl " +
                "получен fileName (String): {}", fileName);
        return Files.readAllBytes(Path.of("avatar/" + fileName));
    }

    @Override
    public String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }
}

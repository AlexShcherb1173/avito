package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.userEntityToUserDTO(user);
    }

    @Override
    @Transactional
    public User updateUserInfo(UpdateUser updateUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());

        usersRepository.save(user);
        return userMapper.userEntityToUserDTO(user);
    }

    @Override
    @Transactional
    public User setUserImage(MultipartFile imageFile) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // Генерация уникального имени файла
        String filename = "user_" + UUID.randomUUID() + "_" +
                (imageFile.getOriginalFilename() != null ?
                        imageFile.getOriginalFilename() : "avatar");

        // Сохранение файла на диск
        Path filePath = Paths.get(uploadDir, "users", filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, imageFile.getBytes());

        // Создание записи в БД (без данных изображения)
        Image newImage = new Image();
        newImage.setFilePath("users/" + filename);
        newImage.setMediaType(imageFile.getContentType());

        // Удаление старого изображения
        if (user.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get(uploadDir, user.getImage().getFilePath()));
            } catch (IOException e) {
                log.error("Failed to delete old image file: {}", user.getImage().getFilePath(), e);
            }
            imageRepository.delete(user.getImage());
        }

        Image savedImage = imageRepository.save(newImage);
        user.setImage(savedImage);
        usersRepository.save(user);

        return userMapper.userEntityToUserDTO(user);
    }

    @Override
    @Transactional
    public boolean registerUser(Register registerForm) {
        if (usersRepository.existsByEmail(registerForm.getUsername())) {
            return false;
        }

        Users newUser = userMapper.registerToUser(registerForm);
        newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        usersRepository.save(newUser);
        return true;
    }

    @Override
    @Transactional
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        usersRepository.save(user);
        return true;
    }
}
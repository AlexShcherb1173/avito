package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String UPLOAD_DIR = "uploads/users/";

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        log.info("Получение информации о текущем пользователе: {}", username);

        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional
    @PreAuthorize("#authentication.name == #username or hasRole('ADMIN')")
    public User updateUser(String username, UpdateUser updateUser, Authentication authentication) {
        log.info("Обновление информации пользователя: {}", username);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (updateUser.getFirstName() != null) {
            user.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            user.setLastName(updateUser.getLastName());
        }
        if (updateUser.getPhone() != null) {
            user.setPhone(updateUser.getPhone());
        }

        UserEntity savedUser = userRepository.save(user);
        log.info("Пользователь {} обновлен", username);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public void updateUserImage(String username, MultipartFile image) throws IOException {
        log.info("Обновление аватара пользователя: {}", username);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(image.getInputStream(), filePath);

        if (user.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get(user.getImage()));
            } catch (IOException e) {
                log.warn("Не удалось удалить старый файл: {}", user.getImage());
            }
        }

        user.setImage(filePath.toString());
        userRepository.save(user);
        log.info("Аватар пользователя {} обновлен", username);
    }

    public UserEntity getUserEntity(String username) {
        log.debug("Получение сущности пользователя: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public byte[] getUserImage(String username) throws IOException {
        log.debug("Получение аватара пользователя: {}", username);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (user.getImage() == null) {
            return new byte[0];
        }

        Path imagePath = Paths.get(user.getImage());
        if (!Files.exists(imagePath)) {
            return new byte[0];
        }

        return Files.readAllBytes(imagePath);
    }
}
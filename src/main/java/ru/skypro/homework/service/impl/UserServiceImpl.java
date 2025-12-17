package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageServiceImpl imageService;
    private final FileStorageConfig fileStorageConfig;

    // Директория для хранения изображений
    private static final String USERS_IMAGE_DIR = "users";
    private static final String BEGIN = "avatar_";

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto, String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        userMapper.updateEntityFromDto(updateUserDto, userEntity);
        UserEntity savedEntity = userRepository.save(userEntity);

        UpdateUserDto result = new UpdateUserDto();
        result.setFirstName(savedEntity.getFirstName());
        result.setLastName(savedEntity.getLastName());
        result.setPhone(savedEntity.getPhone());

        return result;
    }

    @Override
    public boolean updatePassword(NewPasswordDto newPasswordDto, String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), userEntity.getPassword())) {
            return false;
        }

        userEntity.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public boolean updateUserImage(MultipartFile image, String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        // Удаляем старое изображение если есть
        imageService.deleteImage(userEntity.getImage(), USERS_IMAGE_DIR);

        // Сохраняем новое изображение
        String fileName = imageService.saveImage(image, USERS_IMAGE_DIR, BEGIN);

        // Обновляем поле в БД - сохраняем только имя файла
        userEntity.setImage(fileName);
        userRepository.save(userEntity);

        log.info("User image updated for user: {}, filename: {}", username, fileName);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getUserImageById(Integer userId) throws IOException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (userEntity.getImage() == null || userEntity.getImage().isEmpty()) {
            throw new IOException("User has no image: " + userId);
        }

        return imageService.getImage(userEntity.getImage(), USERS_IMAGE_DIR);
    }

    @Override
    public String getUserImageContentTypeById(Integer userId) throws IOException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (userEntity.getImage() == null || userEntity.getImage().isEmpty()) {
            throw new IOException("User has no image: " + userId);
        }

        return imageService.getImageContentType(userEntity.getImage());
    }

    @Override
    public boolean deleteUserImage(String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        boolean deleted = imageService.deleteImage(userEntity.getImage(), USERS_IMAGE_DIR);
        if (deleted) {
            userEntity.setImage(null);
            userRepository.save(userEntity);
        }
        return deleted;
    }

}

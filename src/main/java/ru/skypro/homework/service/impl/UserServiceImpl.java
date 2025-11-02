package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Директория для хранения изображений
    private final Path IMAGE_DIR = Paths.get("uploads", "users");

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto, String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

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
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

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
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        // Создаем директорию если не существует
        if (!Files.exists(IMAGE_DIR)) {
            Files.createDirectories(IMAGE_DIR);
        }

        // Удаляем старое изображение если есть
        deleteUserImage(username);


        return false;
    }

    @Override
    public byte[] getUserImage(Integer userId) throws IOException {
        return new byte[0];
    }

    @Override
    public boolean deleteUserImage(String username) throws IOException {
        return false;
    }
}

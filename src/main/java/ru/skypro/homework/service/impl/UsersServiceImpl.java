package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.FileStorageException;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;


import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Override
    public void setPassword(NewPassword newPassword) {
        User user = currentUserService.getCurrentUser();
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser() {
        User user = currentUserService.getCurrentUser();
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUser) {
        User user = currentUserService.getCurrentUser();
        userMapper.updateEntityFromDto(updateUser, user);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    /// !!!
    @Override
    public void updateImage(Integer userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с Id " + userId + " не найден!"));

    }

    /// !!!!!
    @Override
    public void updateImage(MultipartFile file) {
        User user = currentUserService.getCurrentUser();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        log.info("Creating user with username: {}, email: {}, role: {}", user.getUsername(), user.getUsername(), user.getRole());
        return userRepository.save(user);
    }


}

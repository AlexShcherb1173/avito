package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exceptions.IncorrectPasswordException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void updatePassword(NewPasswordDto dto, Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto getUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UpdateUserDto updateUser(UpdateUserDto dto, Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        UpdateUserDto userDto = userMapper.toUpdateUser(dto, user);
        userRepository.save(user);

        return userDto;
    }

    @Transactional
    @Override
    public void updateUserImage(MultipartFile multipartFile, Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден")
        );
        try {
            if (user.getImage() != null) {
                imageService.deleteImage(user.getImage());
            }
            String filePath = imageService.saveImage(multipartFile, user.getId(), "users");
            user.setImage(filePath);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранение изображения", e);
        }
    }
}

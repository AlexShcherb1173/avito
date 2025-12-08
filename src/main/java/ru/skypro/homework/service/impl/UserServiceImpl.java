package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

/**
 * Реализация {@link UserService} для управления пользователями.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String username) {
        log.info("Getting current user: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(String username, UpdateUser updateUser) {
        log.info("Updating user: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateUser.getFirstName() != null) {
            userEntity.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            userEntity.setLastName(updateUser.getLastName());
        }
        if (updateUser.getPhone() != null) {
            userEntity.setPhone(normalizePhoneNumber(updateUser.getPhone()));
        }

        UserEntity savedEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedEntity);
    }

    /**
     * Нормализует номер телефона для хранения в базе данных.
     * Удаляет лишние пробелы и обрезает до максимальной длины.
     *
     * @param phone исходный номер телефона
     * @return нормализованный номер телефона
     */
    private String normalizePhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }

        String normalized = phone.replaceAll("\\s+", " ").trim();

        if (normalized.length() > 20) {
            log.warn("Phone number too long, truncating: {}", phone);
            normalized = normalized.substring(0, 20);
        }

        return normalized;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserImage(String username, MultipartFile image) {
        log.info("Updating user image: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userEntity.getImage() != null) {
            imageService.deleteImage(userEntity.getImage());
        }

        String newImagePath = imageService.saveImage(image);
        userEntity.setImage(newImagePath);
        userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(String username, String currentPassword, String newPassword) {
        log.info("Updating password for user: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, userEntity.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserEntity(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
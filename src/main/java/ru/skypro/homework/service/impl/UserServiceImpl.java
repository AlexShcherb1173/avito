package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String username) {
        log.info("Getting current user: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public User updateUser(String username, UpdateUser updateUser) {
        log.info("Updating user: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userMapper.updateEntityFromDto(updateUser, userEntity);
        UserEntity savedEntity = userRepository.save(userEntity);

        return userMapper.toDto(savedEntity);
    }

    @Override
    public void updateUserImage(String username, String imagePath) {
        log.info("Updating user image: {}", username);
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userEntity.setImage(imagePath);
        userRepository.save(userEntity);
    }

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

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserEntity(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
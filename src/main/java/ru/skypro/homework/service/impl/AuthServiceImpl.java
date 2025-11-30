package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String username, String password) {
        log.info("Attempting login for user: {}", username);
        try {
            UserEntity userEntity = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return passwordEncoder.matches(password, userEntity.getPassword());
        } catch (UsernameNotFoundException e) {
            log.warn("Login failed for user: {}", username);
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        log.info("Attempting registration for user: {}", register.getUsername());

        if (userRepository.existsByEmail(register.getUsername())) {
            log.warn("Registration failed - user already exists: {}", register.getUsername());
            return false;
        }

        try {
            UserEntity userEntity = userMapper.toEntity(register);
            userEntity.setPassword(passwordEncoder.encode(register.getPassword()));

            if (register.getRole() == null) {
                userEntity.setRole(Role.USER);
            }

            // Нормализуем данные перед сохранением
            normalizeUserData(userEntity);

            userRepository.save(userEntity);
            log.info("User registered successfully: {}", register.getUsername());
            return true;
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during registration for user: {}", register.getUsername(), e);
            return false;
        } catch (Exception e) {
            log.error("Registration failed for user: {}", register.getUsername(), e);
            return false;
        }
    }

    /**
     * Нормализует данные пользователя для сохранения в БД
     */
    private void normalizeUserData(UserEntity userEntity) {
        // Обрезаем поля до максимальной длины
        if (userEntity.getFirstName() != null && userEntity.getFirstName().length() > 16) {
            userEntity.setFirstName(userEntity.getFirstName().substring(0, 16));
        }
        if (userEntity.getLastName() != null && userEntity.getLastName().length() > 16) {
            userEntity.setLastName(userEntity.getLastName().substring(0, 16));
        }
        if (userEntity.getEmail() != null && userEntity.getEmail().length() > 32) {
            userEntity.setEmail(userEntity.getEmail().substring(0, 32));
        }
        if (userEntity.getPhone() != null && userEntity.getPhone().length() > 20) {
            userEntity.setPhone(userEntity.getPhone().substring(0, 20));
        }
    }
}
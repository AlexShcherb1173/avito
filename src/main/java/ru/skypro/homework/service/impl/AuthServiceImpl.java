package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

            userRepository.save(userEntity);
            log.info("User registered successfully: {}", register.getUsername());
            return true;
        } catch (Exception e) {
            log.error("Registration failed for user: {}", register.getUsername(), e);
            return false;
        }
    }
}
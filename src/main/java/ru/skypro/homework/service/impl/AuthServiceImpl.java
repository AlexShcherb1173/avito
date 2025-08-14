package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public boolean login(String username, String password) {
        log.info("Attempting authentication for user: {}", username);
        Optional<Users> user = usersRepository.findByEmail(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            log.info("Authentication successful for user: {}", username);
            return true;
        }
        log.warn("Authentication failed for user: {}", username);
        return false;
    }

    @Override
    public boolean register(Register register) {
        log.info("Registration attempt for user: {}", register.getUsername());

        if (usersRepository.existsByEmail(register.getUsername())) {
            log.warn("Registration failed - user already exists: {}", register.getUsername());
            return false;
        }

        try {
            Users user = userMapper.registerToUser(register);
            user.setPassword(passwordEncoder.encode(register.getPassword()));
            usersRepository.save(user);
            log.info("User registered successfully: {}", register.getUsername());
            return true;
        } catch (Exception e) {
            log.error("Registration error for user: {}", register.getUsername(), e);
            return false;
        }
    }
}
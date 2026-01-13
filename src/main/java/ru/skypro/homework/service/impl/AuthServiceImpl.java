package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        log.debug("Попытка входа для пользователя: {}", userName);

        try {
            UserDetails userDetails = loadUserByUsername(userName);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                log.warn("Неверный пароль для пользователя: {}", userName);
                return false;
            }

            if (!userDetails.isEnabled()) {
                log.warn("Аккаунт пользователя отключен: {}", userName);
                return false;
            }

            log.info("Успешный вход пользователя: {}", userName);
            return true;

        } catch (UsernameNotFoundException e) {
            log.warn("Пользователь не найден: {}", userName);
            return false;
        } catch (Exception e) {
            log.error("Ошибка входа для пользователя {}: {}", userName, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        log.debug("Попытка регистрации для пользователя: {}", register.getUsername());

        if (userRepository.existsByUsername(register.getUsername())) {
            log.warn("Пользователь уже существует: {}", register.getUsername());
            return false;
        }

        if (!isValidRegistrationData(register)) {
            log.warn("Неверные данные регистрации для пользователя: {}", register.getUsername());
            return false;
        }

        try {
            UserEntity user = UserEntity.builder()
                    .username(register.getUsername().toLowerCase().trim())
                    .password(passwordEncoder.encode(register.getPassword()))
                    .firstName(register.getFirstName().trim())
                    .lastName(register.getLastName().trim())
                    .phone(register.getPhone() != null ? register.getPhone().trim() : null)
                    .role(register.getRole() != null ? register.getRole() : Role.USER)
                    .enabled(true)
                    .build();

            userRepository.save(user);

            log.info("Пользователь успешно зарегистрирован: {}", register.getUsername());
            return true;

        } catch (Exception e) {
            log.error("Ошибка регистрации для пользователя {}: {}", register.getUsername(), e.getMessage());
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Загрузка пользователя по username: {}", username);

        return userRepository.findByUsername(username.toLowerCase().trim())
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .disabled(!user.isEnabled())
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    private boolean isValidRegistrationData(Register register) {
        if (register.getUsername() == null || register.getUsername().trim().isEmpty()) {
            return false;
        }

        if (register.getPassword() == null || register.getPassword().trim().isEmpty()) {
            return false;
        }

        if (register.getFirstName() == null || register.getFirstName().trim().isEmpty()) {
            return false;
        }

        if (register.getLastName() == null || register.getLastName().trim().isEmpty()) {
            return false;
        }

        if (!register.getUsername().contains("@")) {
            return false;
        }

        if (register.getPassword().length() < 8) {
            return false;
        }

        return true;
    }
}
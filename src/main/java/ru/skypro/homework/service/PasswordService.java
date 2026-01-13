package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.repository.UserRepository;

@Service
@Transactional
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        log.info("Смена пароля пользователя: {}", username);

        if (newPassword == null || newPassword.trim().length() < 8) {
            log.warn("Новый пароль слишком короткий: минимум 8 символов");
            return false;
        }

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(currentPassword, user.getPassword()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    log.info("Пароль успешно изменен для пользователя: {}", username);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Пользователь не найден или неверный пароль: {}", username);
                    return false;
                });
    }
}
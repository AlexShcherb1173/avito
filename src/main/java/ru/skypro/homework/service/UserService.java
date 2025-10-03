package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для работы с пользователями.
 * Включает логику смены пароля пользователя.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Меняет пароль пользователя.
     * Проверяет текущий пароль, затем обновляет его на новый.
     *
     * @param username имя пользователя
     * @param currentPassword текущий пароль пользователя
     * @param newPassword новый пароль
     * @return true, если пароль был успешно изменен, иначе false
     */
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(username);
        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}

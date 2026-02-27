package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Сервис аутентификации и регистрации пользователей.
 * <p>
 * Регистрация сохраняет пользователя в БД, пароль хранится в виде хеша (BCrypt).
 * Проверка логина выполняется сравнением введённого пароля с хешем из БД.
 * </p>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean register(Register register) {
        if (register == null) return false;

        String email = register.getUsername();
        String rawPassword = register.getPassword();

        if (email == null || email.isBlank()) return false;
        if (rawPassword == null || rawPassword.isBlank()) return false;

        if (register.getFirstName() == null || register.getFirstName().isBlank()) return false;
        if (register.getLastName() == null || register.getLastName().isBlank()) return false;

        if (userRepository.findByEmail(email).isPresent()) return false;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName(register.getFirstName().trim());
        user.setLastName(register.getLastName().trim());
        user.setPhone(register.getPhone());
        user.setRole(UserRole.USER);

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


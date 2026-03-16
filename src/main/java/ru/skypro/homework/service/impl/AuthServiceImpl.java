package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

/**
 * Сервис для работы с авторизацией и регистрацией пользователей.
 * Отвечает за создание пользователей, проверку данных
 * и изменение пароля.
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Выполняет проверку учетных данных пользователя.
     *
     * @param userName email пользователя
     * @param password пароль пользователя
     * @return true если авторизация успешна, иначе false
     */

    @Override
    public boolean login(String userName, String password) {

        Optional<User> userOptional = userRepository.findByEmail(userName);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register данные пользователя для регистрации
     * @return true если регистрация успешна, иначе false
     */

    @Override
    public boolean register(Register register) {

        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }

        User user = new User();
        user.setEmail(register.getUsername());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());

        // Преобразование String → Enum Role
        user.setRole(Role.valueOf(register.getRole()));

        user.setImage(null);

        userRepository.save(user);

        return true;
    }
}
package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса авторизации и регистрации пользователей.
 *
 * Содержит бизнес-логику:
 * - входа пользователя в систему
 * - регистрации нового пользователя
 * - смены пароля пользователя
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Проверяет корректность логина и пароля пользователя.
     *
     * @param userName email пользователя
     * @param password пароль пользователя
     * @return true если пользователь найден и пароль совпадает
     */
    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register DTO с данными регистрации
     * @return true если пользователь успешно зарегистрирован
     */
    @Override
    public boolean register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }

        UserEntity userEntity = userMapper.fromRegisterDto(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));

        if (userEntity.getRole() == null || userEntity.getRole().isBlank()) {
            userEntity.setRole(Role.USER.name());
        }

        userRepository.save(userEntity);
        return true;
    }

    /**
     * Меняет пароль пользователя.
     *
     * @param username email пользователя
     * @param oldPassword текущий пароль
     * @param newPassword новый пароль
     * @return true если пароль успешно изменён
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userRepository.findByEmail(username)
                .map(user -> {
                    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                        return false;
                    }

                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
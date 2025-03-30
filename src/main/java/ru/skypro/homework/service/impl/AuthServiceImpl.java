package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Сервис аутентификации и регистрации пользователей.
 * Реализует методы для входа и регистрации.
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = passwordEncoder;
    }


    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param userName Имя пользователя (email).
     * @param password Пароль пользователя.
     * @return true, если аутентификация успешна, иначе false.
     */
    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }


    /**
     * Регистрирует нового пользователя.
     *
     * @param registerDTO Данные для регистрации.
     * @return true, если регистрация успешна, иначе false.
     */
    @Override
    public boolean register(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getUsername()).isPresent()) {
            return false;
        }
        log.info(registerDTO.toString());
        User user = new User();
        user.setEmail(registerDTO.getUsername());
        user.setPassword(encoder.encode(registerDTO.getPassword()));
        user.setRole(Role.valueOf(registerDTO.getRole().name()));
        user.setEnabled(true);
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setPhone(registerDTO.getPhone());
        log.info(user.toString());

        userRepository.save(user);
        return true;
    }
}

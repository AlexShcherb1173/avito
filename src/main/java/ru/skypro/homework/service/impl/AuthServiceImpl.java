package ru.skypro.homework.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public boolean login(String userName, String password) {
        try {
            // Создаем объект для аутентификации
            Authentication authToken = new UsernamePasswordAuthenticationToken(userName, password);

            // Аутентифицируем пользователя
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Устанавливаем аутентификацию в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Успешный вход пользователя: {}", userName);
            return true;

        } catch (Exception e) {
            log.warn("Неверные учётные данные для пользователя: {}", userName);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean register(Register registerReq) {
        try {
            // Проверяем, не существует ли уже пользователь с таким username
            if (userRepository.existsByUsername(registerReq.getUsername())) {
                log.warn("Пользователь с username {} уже существует", registerReq.getUsername());
                return false;
            }

            // Создаем нового пользователя
            User user = new User();
            user.setUsername(registerReq.getUsername());
            user.setPassword(passwordEncoder.encode(registerReq.getPassword())); // Кодируем пароль!
            user.setFirstName(registerReq.getFirstName());
            user.setLastName(registerReq.getLastName());
            user.setPhone(registerReq.getPhone());
            user.setRole(registerReq.getRole()); // Убедитесь, что роль устанавливается правильно

            // Сохраняем пользователя в базу данных
            userRepository.save(user);

            log.info("Зарегистрирован новый пользователь: {}", registerReq.getUsername());
            return true;

        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", e.getMessage());
            return false;
        }
    }
}























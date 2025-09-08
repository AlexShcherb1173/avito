package ru.skypro.homework.service.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 * Обрабатывает регистрацию, смену пароля и (при необходимости) авторизацию.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    /**
     * Регистрирует нового пользователя.
     * Проверяет, что имя и пароль заданы, и что пользователь с таким email ещё не существует.
     *
     * @param dto DTO с данными нового пользователя
     * @throws IllegalArgumentException если имя пользователя или пароль пусты
     * @throws IllegalStateException если пользователь с таким email уже существует
     */
    @Override
    public void register(Register dto) {
        if (!StringUtils.hasText(dto.getUsername()) || !StringUtils.hasText(dto.getPassword())) {
            throw new IllegalArgumentException("username/password required");
        }
        users.findByEmail(dto.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("user exists");
        });
        UserEntity u = new UserEntity();
        u.setEmail(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhone(dto.getPhone());
        u.setRole(dto.getRole() == null ? Role.USER : Role.valueOf(dto.getRole().name()));
        users.save(u);
    }

    /**
     * Метод-заглушка для авторизации (не используется напрямую, так как авторизация происходит через Spring Security).
     *
     * @param dto DTO с логином и паролем
     */
    @Override
    public void login(Login dto) {
        // intentionally left blank (handled by Spring Security)
    }

    /**
     * Меняет пароль пользователя, если текущий пароль указан верно.
     *
     * @param username email пользователя (берётся из Authentication)
     * @param dto DTO с текущим и новым паролем
     * @throws BadCredentialsException если текущий пароль неверен или пользователь не найден
     */
    @Override
    public void changePassword(String username, NewPassword dto) {
        UserEntity u = users.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("not found"));
        if (!encoder.matches(dto.getCurrentPassword(), u.getPassword())) {
            throw new BadCredentialsException("bad current password");
        }
        u.setPassword(encoder.encode(dto.getNewPassword()));
        users.save(u);
    }
}

package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.PasswordService;

import javax.validation.Valid;

@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final PasswordService passwordService;

    public AuthController(AuthService authService, PasswordService passwordService) {
        this.authService = authService;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login login) {
        log.info("Попытка входа пользователя: {}", login.getUsername());

        boolean isAuthenticated = authService.login(login.getUsername(), login.getPassword());

        if (isAuthenticated) {
            log.info("Успешный вход пользователя: {}", login.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Неудачная попытка входа пользователя: {}", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Register register) {
        log.info("Регистрация нового пользователя: {}", register.getUsername());

        boolean isRegistered = authService.register(register);

        if (isRegistered) {
            log.info("Пользователь успешно зарегистрирован: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Ошибка регистрации пользователя: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/users/set_password")
    public ResponseEntity<?> setPassword(@Valid @RequestBody NewPassword newPassword,
                                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        log.info("Запрос на смену пароля для пользователя: {}", username);

        boolean success = passwordService.changePassword(
                username,
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );

        if (success) {
            log.info("Пароль успешно изменен для пользователя: {}", username);
            return ResponseEntity.ok().build();
        } else {
            log.warn("Не удалось изменить пароль для пользователя: {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Запрос на выход пользователя");
        return ResponseEntity.ok().build();
    }
}
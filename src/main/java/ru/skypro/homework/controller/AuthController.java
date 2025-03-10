package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

/**
 * Контроллер для управления авторизацией и регистрацией пользователей.
 * <p>
 * Этот контроллер предоставляет API для выполнения операций входа и регистрации пользователей.
 * </p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController

public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping("/login")
    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("1 login");
        try {
            if (authService.login(loginDto.getUsername(), loginDto.getPassword())) {
                return ResponseEntity.ok().body("Авторизация успешна");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при авторизации: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {

        if (authService.register(registerDto)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким email уже существует");
        }
    }
}

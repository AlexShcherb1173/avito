package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

/**
 * REST-контроллер для обработки запросов аутентификации и регистрации.
 * Предоставляет endpoints для входа в систему и создания новых учетных записей.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Выполняет аутентификацию пользователя.
     * Фактическая проверка учетных данных выполняется Spring Security.
     *
     * @param login DTO с данными для входа (логин и пароль)
     */
    @PostMapping("/login")
    public void login(@RequestBody LoginDto login) {
        // Аутентификация обрабатывается Spring Security
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register DTO с данными для регистрации нового пользователя
     * @throws RuntimeException если пользователь с таким email уже существует
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterDto register) {
        boolean registered = authService.register(register);
        if (!registered) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
    }
}
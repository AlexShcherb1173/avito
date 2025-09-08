package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает запросы на создание нового пользователя и вход в систему.
 */
@RestController
@Tag(name = "Регистрация/Авторизация")
public class AuthController {

    private final AuthService service;

    /**
     * Конструктор контроллера.
     *
     * @param service сервис авторизации
     */
    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param dto DTO с данными для регистрации
     */
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid Register dto) {
        service.register(dto);
    }

    /**
     * Аутентификация пользователя (логин).
     * Важно: метод ничего не возвращает — Basic Auth отрабатывает через Spring Security.
     *
     * @param dto DTO с логином и паролем
     */
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public void login(@RequestBody @Valid Login dto) {
        service.login(dto);
    }
}

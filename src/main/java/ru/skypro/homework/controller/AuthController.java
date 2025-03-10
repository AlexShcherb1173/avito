package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для обработки аутентификации пользователей.
 * Этот класс предоставляет REST API для входа в систему.
 * Используется аннотация {@link CrossOrigin} для разрешения кросс-доменных запросов с фронтенда.
 * Логирование осуществляется с помощью {@link Slf4j}.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "Методы для работы авторизации")
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизует пользователя по переданным учетным данным.
     *
     * @param login объект {@link LoginDTO}, содержащий имя пользователя и пароль.
     * @return {@link ResponseEntity} с кодом 200 (ОК) при успешной аутентификации
     * или 401 (UNAUTHORIZED) при неверных учетных данных.
     */
    @Operation(summary = "Авторизация пользователя")
    @ApiResponse(responseCode = "200", description = "Успешная авторизация")
    @ApiResponse(responseCode = "401", description = "Ошибка авторизации (неверные учетные данные)")
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


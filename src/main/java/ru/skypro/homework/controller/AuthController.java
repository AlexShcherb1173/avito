package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Slf4j// Логирование
@CrossOrigin(value = "http://localhost:3000")// Разрешаем запросы с фронтенда
@RestController// Указываем что это REST контроллер
@RequiredArgsConstructor  // Lombok - автоматически создает конструктор с final полями
//аутентификация и регистрация:
public class AuthController {

    private final AuthService authService;// Внедряем сервис аутентификации

    @PostMapping("/login")// Обработка POST запросов на /login
    @Operation(tags = {"Авторизация"})
    public ResponseEntity<?> login(@RequestBody Login login) {
        // @RequestBody - данные приходят в теле запроса в формате JSON
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();// 200 OK если успешно
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();// 401 если ошибка

        }
    }

    @PostMapping("/register")
    @Operation(tags = {"Регистрация"})
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

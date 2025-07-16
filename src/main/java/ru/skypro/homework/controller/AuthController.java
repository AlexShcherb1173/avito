package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final String TEST_USERNAME = "user@example.com";
    private  String TEST_PASSWORD = "password";

    // Авторизация (соответствует /login из OpenAPI)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (TEST_USERNAME.equals(login.getUsername()) &&
                TEST_PASSWORD.equals(login.getPassword())) {
            // Генерация и возврат токена
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Регистрация (соответствует /register из OpenAPI)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (register.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Пароль должен быть не менее 8 символов");
        }
        return ResponseEntity.status(201).build();
    }
}
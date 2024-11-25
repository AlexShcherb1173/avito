package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Регистрация и авторизация")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<String> login(@RequestBody Login login) {
        log.info("Вы вошли в метод login");
        if (login == null || login.getUsername() == null || login.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный запрос");
        }
        try {
            if (authService.login(login.getUsername(), login.getPassword())) {
                log.info("Успешная авторизация пользователя: {}", login.getUsername());
                return ResponseEntity.ok().build();
            } else {
                log.warn("Такого пользователя не существует: {}", login.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.error("Ошибка аутентификации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка аутентификации");
        }
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = ""))
    })
    public ResponseEntity<String> register(@RequestBody Register register) {
        log.info("Вы вошли в метод register");
        if (register == null) {
            log.warn("Неверный запрос: register не может быть null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный запрос");
        }
        try {
            if (authService.register(register)) {
                log.info("Регистрация прошла успешно");
                return ResponseEntity.status(HttpStatus.CREATED).body("Регистрация успешна");
            } else {
                log.warn("Регистрация не прошла: пользователь с таким username уже существует");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с таким username уже существует");
            }
        } catch (Exception e) {
            log.error("Ошибка регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка регистрации");
        }
    }
}

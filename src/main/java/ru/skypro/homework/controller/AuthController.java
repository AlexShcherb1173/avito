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
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Вы вошли в метод login");
        if (authService.login(login.getUsername(), login.getPassword())) {
            log.info("Успешная авторизация");
            return ResponseEntity.ok().build();
        } else {
            log.info("Такого пользователя не существует");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> register(@RequestBody Register register) {
        log.info("Вы вошли в метод register");
        if (authService.register(register)) {
            log.info("Регистрация прошла успешно");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.info("Регистрация не прошла");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

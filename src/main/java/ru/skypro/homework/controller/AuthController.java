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
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public boolean login(@RequestBody Login login) {
        return authService.login(login.getUsername(), login.getPassword());
    }

    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public boolean register(@RequestBody Register register) {
        return authService.register(register);
    }

}

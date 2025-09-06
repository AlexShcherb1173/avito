package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@RestController
@Tag(name = "Регистрация/Авторизация")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid Register dto) { service.register(dto); }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public void login(@RequestBody @Valid Login dto) { service.login(dto); }
}

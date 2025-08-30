package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")

@RestController
@RequestMapping
@Tag(name = "Авторизация и регистрация", description = "Управление регистрацией и входом пользователей")
public class AuthController {

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<Void> register(@RequestBody Register register) {
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<Void> login(@RequestBody Login login) {
        return ResponseEntity.ok().build();
    }
}
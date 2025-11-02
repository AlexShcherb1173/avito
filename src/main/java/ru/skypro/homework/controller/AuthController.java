package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.pro.packaged.O;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.user.Login;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Регистрация", description = "API для регистрации пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя в системе"
    )
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Выход из системы")
    @ApiResponse(responseCode = "200", description = "Успешный выход")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        SecurityContextHolder.clearContext();   //очищаем контекст безопасности
        return ResponseEntity.ok().build();
    }
}

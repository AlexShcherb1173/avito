package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            tags = {"Авторизация"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Attempting login for user: {}", login.getUsername());
        if (authService.login(login.getUsername(), login.getPassword())) {
            log.info("User {} successfully logged in", login.getUsername());
            return ResponseEntity.ok().build();
        }
        log.warn("Login failed for user: {}", login.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже существует")
            }
    )
    public ResponseEntity<?> register(@RequestBody Register request) {
        log.info("Registration request for: {}", request.getUsername());

        Register register = new Register();
        register.setUsername(request.getUsername());
        register.setPassword(request.getPassword());
        register.setFirstName(request.getFirstName());
        register.setLastName(request.getLastName());
        register.setPhone(request.getPhone());
        register.setRole(request.getRole());

        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
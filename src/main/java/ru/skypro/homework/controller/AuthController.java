package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RestController
@Tag(name = " Авторизация", description = "API для аутентификации и регистрации")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "авторизация пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),

            @ApiResponse(responseCode = "401", description = "Unauthorized")
    }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Login attempt for user: {}", login.getUsername());
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "регистрация пользователя", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),

            @ApiResponse(responseCode = "400", description = "Bad Request")
    }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        log.info("Registration attempt for user: {}", register.getUsername());
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

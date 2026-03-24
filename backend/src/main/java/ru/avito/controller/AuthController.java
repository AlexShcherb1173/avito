package ru.avito.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.LoginResponse;
import ru.avito.dto.auth.RegisterRequest;
import ru.avito.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(
                        true,
                        "User registered successfully"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        boolean success = authService.login(request);

        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            false,
                            "Invalid credentials"
                    ));
        }

        return ResponseEntity.ok(
                new LoginResponse(
                        true,
                        "Login successful"
                )
        );
    }
}
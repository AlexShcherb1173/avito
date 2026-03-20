package ru.avito.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.RegisterRequest;
import ru.avito.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        boolean success = authService.login(request);

        if (!success) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok().build();
    }
}
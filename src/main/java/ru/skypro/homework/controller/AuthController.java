package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")

@Tag(name = "Auth", description = "Controller for user authentication and registration")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login", description = "Authenticates a user and returns a token")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Login login(@RequestBody Login login) {
        return login;
    }

    @Operation(summary = "User registration", description = "Registers a new user and returns user details")
    @PostMapping("/register")
    public UserResponse register(@RequestBody Register register) {
        return new UserResponse();
    }
}

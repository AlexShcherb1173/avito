package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Авторизация пользователя", operationId = "login", tags = {"Авторизация"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public void login(@RequestBody Login login) {
        if (!authService.login(login.getUsername(), login.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Регистрация пользователя", operationId = "register", tags = {"Регистрация"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@RequestBody Register register) {
        if (!authService.register(register)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}


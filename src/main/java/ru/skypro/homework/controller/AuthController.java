package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthServiceApi;
import ru.skypro.homework.utils.ValidationUtils;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceApi authServiceApi;

    @Operation(
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "string"
                                    )
                            )
                    )
            },
            tags = "Авторизация"
    )
    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestBody @Valid Login login,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.UNAUTHORIZED);
        }
        if (authServiceApi.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD_REQUEST",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "string"
                                    )
                            )
                    )
            },
            tags = "Регистрация"
    )
    @PostMapping("register")
    public ResponseEntity<?> register(
            @RequestBody @Valid Register register,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if (authServiceApi.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

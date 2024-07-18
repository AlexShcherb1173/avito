package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.RegisterService;

/**
 * Контроллер для регистрации пользователей
 */
@RestController
@RequestMapping("/register")
@Tag(name = "Регистрация пользователя", description = "Эндпойнты для работы с пользователями")
@AllArgsConstructor
public class RegisterController {

    private RegisterService service;

    @PostMapping
    @Operation(summary = "Регистрация пользователя",
            operationId = "register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        return null;
    }
}

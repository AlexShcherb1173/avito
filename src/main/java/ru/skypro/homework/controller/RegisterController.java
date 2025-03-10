package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для регистрации пользователей.
 * Этот контроллер предоставляет API для регистрации новых пользователей в системе.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(name = "Регистрация", description = "Методы для работы с регистрации")
public class RegisterController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя.
     *
     * @param register DTO, содержащий данные для регистрации пользователя.
     * @return {@link ResponseEntity} со статусом 201 (Created) при успешной регистрации,
     * или статусом 400 (Bad Request), если регистрация не удалась.
     */
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Ошибка регистрации")
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody RegisterDTO register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


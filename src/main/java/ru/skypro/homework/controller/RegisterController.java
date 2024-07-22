/*
package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.impl.RegisterServiceImpl;

*/
/**
 * Контроллер для регистрации пользователей
 *//*

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/registers")
@Tag(name = "Регистрация пользователя", description = "Эндпойнты для работы с пользователями")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterServiceImpl service;

    @PostMapping
    @Operation(summary = "Регистрация пользователя",
            operationId = "register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto register) {
        return null;
    }
}
*/

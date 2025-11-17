package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skypro.homework.dto.LoginDto;

public interface AuthControllerInterface {
    @PostMapping("/login")
    @Operation(tags = {"Авторизация"}, summary = "Авторизация пользователя", operationId = "login")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "401", description = "Unauthorized")})
    void login(@RequestBody LoginDto loginDto);
}

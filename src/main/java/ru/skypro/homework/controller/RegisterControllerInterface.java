package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skypro.homework.dto.RegisterDto;

import javax.validation.Valid;

public interface RegisterControllerInterface {
    @PostMapping("/register")
    @Operation(tags = {"Регистрация"}, summary = "Регистрация пользователя", operationId = "register")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"), @ApiResponse(responseCode = "400", description = "Bad Request")})
    void register(@Valid @RequestBody RegisterDto registerDto);
}

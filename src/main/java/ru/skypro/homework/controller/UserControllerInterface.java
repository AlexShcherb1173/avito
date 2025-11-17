package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

public interface UserControllerInterface {
    @PostMapping("/set_password")
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "setPassword", summary = "Обновление пароля", tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    void setPassword(@RequestBody NewPasswordDto newPasswordDto);

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "getUser", summary = "Получение информации об авторизованном пользователе", tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    UserDto getUser();

    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "updateUser", summary = "Обновление информации об авторизованном пользователе", tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUserDto);

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "updateUserImage", summary = "Обновление аватара авторизованного пользователя", tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    void updateUserImage(@RequestParam("image") MultipartFile image) throws IOException;
}

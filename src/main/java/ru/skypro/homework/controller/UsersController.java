package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.UsersServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для получения и изменения информации пользователя")
public class UsersController {
    private final UsersServiceImpl usersService;

    @PostMapping("/set_password")
    @Operation(operationId = "setPassword",
            summary = "Обновление пароля",
            tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public void setPassword(@RequestBody NewPassword newPassword) {
    }

    @GetMapping("/me")
    @Operation(operationId = "getUser",
            summary = "Получение информации об авторизованном пользователе",
            tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public User getUser() {
        return new User();
    }

    @PatchMapping("/me")
    @Operation(operationId = "updateUser",
            summary = "Обновление информации об авторизованном пользователе",
            tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        return new UpdateUser();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(operationId = "updateUserImage",
            summary = "Обновление аватара авторизованного пользователя",
            tags = {"Пользователи"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public void updateUserImage(@RequestParam("image") MultipartFile image) {
    }
}

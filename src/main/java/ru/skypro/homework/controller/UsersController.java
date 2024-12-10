package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UsersService;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public String setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return usersService.setPassword(newPasswordDto);

    }
    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public UserDto getUser() {
        return usersService.getUser();

    }
    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public String updateUser() {
        return usersService.updateUser();

    }

    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public String updateUserImage() {
        return null;

    }

}

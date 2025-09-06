package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping
@Tag(name = "Пользователи")
public class UsersController {
    private final UserService service;
    public UsersController(UserService service) { this.service = service; }

    @Operation(summary = "Обновление пароля")
    @PostMapping("/users/set_password")
    public void setPassword(@RequestBody @Valid NewPassword dto) {
        service.setPassword(dto);
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/users/me")
    public User getMe() { return service.getMe(); }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/users/me")
    public UpdateUser updateMe(@RequestBody @Valid UpdateUser dto) {
        return service.updateMe(dto);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(value = "/users/me/image", consumes = "multipart/form-data")
    public void updateUserImage(@RequestPart("image") MultipartFile image) {
        service.updateUserImage(image);
    }
}

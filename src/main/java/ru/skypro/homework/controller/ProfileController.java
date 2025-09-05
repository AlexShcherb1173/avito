package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;


@RestController
@RequestMapping("/profile")
@Tag(name = "Профиль", description = "Работа с профилем текущего пользователя")
public class ProfileController {

    @GetMapping
    @Operation(summary = "Получение данных профиля")
    public ResponseEntity<UserDto> getProfile() {
        return ResponseEntity.ok(new UserDto());
    }

    @PatchMapping
    @Operation(summary = "Обновление данных профиля")
    public ResponseEntity<UpdateUser> updateProfile(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }
}
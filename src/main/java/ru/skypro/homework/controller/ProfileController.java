package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;


@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "Профиль", description = "Работа с профилем текущего пользователя")
public class ProfileController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получение данных профиля")
    public ResponseEntity<UserDto> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PatchMapping
    @Operation(summary = "Обновление данных профиля")
    public ResponseEntity<UpdateUser> updateProfile(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }
}
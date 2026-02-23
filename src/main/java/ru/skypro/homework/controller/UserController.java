package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {

        User user = new User();
        user.setId(1);
        user.setEmail("user@gmail.com");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+79991234567");
        user.setRole(Role.USER.name());   // ВОТ ТУТ ИСПРАВЛЕНО
        user.setImage("/images/avatar.jpg");

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}
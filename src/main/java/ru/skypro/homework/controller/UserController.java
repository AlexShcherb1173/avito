package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Registration.Password;
import ru.skypro.homework.dto.Registration.UpdateUser;
import ru.skypro.homework.dto.User.User;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи", description = "Операции с пользователями")
@RequestMapping("/users")
public class UserController {


    @Tag(name = "Смена пароля", description = "Смена пароля")
    @PostMapping("/set_password")
    public Password setPassword(@RequestBody Password newPassword) {
        return newPassword;
    }

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping("/me")
    public User getUser() {
        User emptyUser = new User();
        return emptyUser;
    }

    @Operation(summary = "Обновление информации о пользователе")
    @PatchMapping("/me")
    public User updateUser(@RequestBody UpdateUser updateUser) {
        User emptyUser = new User();
        return emptyUser;
    }

    @Operation(summary = "Обновление аватара пользователя")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}


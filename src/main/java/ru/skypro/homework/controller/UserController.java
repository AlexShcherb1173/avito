package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Password;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи", description = "Операции с пользователями")
@RequestMapping("/users")
public class UserController {

    /**
     * Смена пароля
     */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody Password newPassword) {
        return ResponseEntity.ok().build();
    }

    /**
     * Получение информации о текущем пользователе
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        User emptyUser = new User();
        return ResponseEntity.ok(emptyUser);
    }

    /**
     * Обновление информации о пользователе
     */
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        User emptyUser = new User();
        return ResponseEntity.ok(emptyUser);
    }

    /**
     * Обновление аватара пользователя
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}


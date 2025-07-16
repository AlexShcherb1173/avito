package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.comment.UpdateUser;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private  String TEST_PASSWORD = "password";

    // Смена пароля (соответствует /users/set_password из OpenAPI)
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {
        if (!TEST_PASSWORD.equals(newPassword.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TEST_PASSWORD = newPassword.getNewPassword(); // В реальном приложении сохранять в БД
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = new User();
        user.setEmail("user@example.com");
        return ResponseEntity.ok(user);
    }
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        User user = new User();
        // Обновление полей пользователя
        return ResponseEntity.ok(user);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image) {
        // Обработка изображения
        return ResponseEntity.ok("Image updated");
    }
}
package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@AuthenticationPrincipal User user,
                                              @RequestBody String newPassword) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        userService.updatePassword(user, newPassword);
        return ResponseEntity.ok("Пароль успешно обновлен");
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateUserInfo(@AuthenticationPrincipal User user,
                                                 @RequestBody User updatedUserInfo) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        userService.updateUserInfo(user, updatedUserInfo);
        return ResponseEntity.ok("Информация о пользователе успешно обновлена");
    }

    @PatchMapping("/me/image")
    public ResponseEntity<String> updateUserImage(@AuthenticationPrincipal User user,
                                                  @RequestParam("image") MultipartFile image) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        userService.updateUserImage(user, image);
        return ResponseEntity.ok("Аватар пользователя успешно обновлен");
    }
}
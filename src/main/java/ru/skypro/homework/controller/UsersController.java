package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.util.Map;

@RestController
@RequestMapping({"/api/users", "/users"})
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

//    @GetMapping("/me")
//    public ResponseEntity<User> getMe(Authentication authentication) {
//        String email = authentication.getName();
//        return ResponseEntity.ok(userService.getUserByEmail(email));
//    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        // Проверка на null
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Not authenticated",
                    "message", "Пожалуйста, войдите в систему"
            ));
        }

        // Получаем email
        String email = authentication.getName();
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "No email in authentication",
                    "message", "Ошибка авторизации"
            ));
        }

        try {
            // Получаем пользователя
            User user = userService.getUserByEmail(email);

            // Убедимся что все поля заполнены
            if (user.getRole() == null) {
                user.setRole("USER");
            }
//            if (user.getAvatar() == null) {
//                user.setAvatar("/default-avatar.png");
//            }
//            // enabled уже должно быть true из маппера

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            // Логируем ошибку
            e.printStackTrace();

            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "message", "Пользователь не найден",
                    "type", e.getClass().getSimpleName()
            ));
        }
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateMe(@RequestBody UpdateUser updateUser,
                                         Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateUserByEmail(email, updateUser));
    }

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword,
                                            Authentication authentication) {
        String email = authentication.getName();
        userService.setPassword(email, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/me/image", consumes = "multipart/form-data", produces = "application/octet-stream")
    public ResponseEntity<byte[]> updateImage(@RequestParam("image") MultipartFile image,
                                              Authentication authentication) {
        String email = authentication.getName();

        String imagePath = (image != null && image.getOriginalFilename() != null)
                ? image.getOriginalFilename()
                : null;

        return ResponseEntity.ok(userService.updateUserImage(email, imagePath));
    }
}

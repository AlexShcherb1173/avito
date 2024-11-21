package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.NewPassword;
import ru.skypro.homework.UpdateUser;
import ru.skypro.homework.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService; // Сервис для работы с пользователями

    @PostMapping("/set_password")
    public String setPassword(@RequestBody NewPassword newPassword) {
        // Логика обновления пароля
        boolean isUpdated = userService.updatePassword(newPassword.getCurrentPassword(), newPassword.getNewPassword());
        if (isUpdated) {
            return "Пароль успешно обновлён";
        } else {
            throw new RuntimeException("Ошибка обновления пароля");
        }
    }

    @GetMapping("/me")
    public User getUser() {
        // Логика получения информации о текущем пользователе
        return userService.getCurrentUser();
    }

    @PatchMapping("/me")
    public String updateUser(@RequestBody UpdateUser updateUser) {
        // Логика обновления информации о пользователе
        userService.updateUser(updateUser);
        return "Информация о пользователе обновлена";
    }

    @PatchMapping("/me/image")
    public String updateUserImage(@RequestParam("image") MultipartFile image) {
        // Логика обновления аватара пользователя
        userService.updateUserImage(image);
        return "Аватар успешно обновлён";
    }
}

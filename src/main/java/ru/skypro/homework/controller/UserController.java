package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.Login;
//import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

//    @Autowired
//    private UserService userService; // Сервис для работы с пользователями

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public String setPassword(@RequestBody NewPassword newPassword) {
        // Логика обновления пароля
//        boolean isUpdated = userService.updatePassword(newPassword.getCurrentPassword(), newPassword.getNewPassword());
//        if (isUpdated) {
//            return "Пароль успешно обновлён";
//        } else {
//            throw new RuntimeException("Ошибка обновления пароля");
//        }
        return null;
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public User getUser() {
        // Логика получения информации о текущем пользователе
//        return userService.getCurrentUser();
        return null;
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public String updateUser(@RequestBody UpdateUser updateUser) {
        // Логика обновления информации о пользователе
//        userService.updateUser(updateUser);
//        return "Информация о пользователе обновлена";
        return null;
    }

    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public String updateUserImage(@RequestParam("image") MultipartFile image) {
        // Логика обновления аватара пользователя
//        userService.updateUserImage(image);
//        return "Аватар успешно обновлён";
        return null;
    }
}

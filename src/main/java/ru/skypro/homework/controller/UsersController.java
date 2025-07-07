package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // Обновление пароля
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword) throws ForbiddenException {
        usersService.setPassword(newPassword);
    }

    // Получение информации об авторизованном пользователе
    @GetMapping("/me")
    public User getUser() {
        return usersService.getUser();
    }

    // Обновление информации об авторизованном пользователе
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser user) {
        return usersService.updateUser(user);
    }

    // Обновление аватара авторизованного пользователя
    @PatchMapping(path = "/me/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateUserImage(@RequestPart MultipartFile image) throws IOException {
        usersService.updateUserImage(image);
    }
}

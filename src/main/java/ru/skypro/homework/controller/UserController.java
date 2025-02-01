package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Пользватели")
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public boolean setPassword(@RequestParam String userName,
                               @RequestParam String password,
                               @RequestParam String newPassword) {
        return userService.setPassword(userName, password, newPassword);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public UserDTO showMe(@RequestBody Login login) {
        return userService.showUserInfo(login);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public void updateUserInfo(@RequestBody Login login,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String phone) {
        userService.updateUserInfo(login, firstName, lastName, phone);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public void uploadImage(@RequestBody Login login,
                            @RequestBody MultipartFile multipartFile) throws IOException {
        userService.uploadAvatar(login, multipartFile);
    }
}

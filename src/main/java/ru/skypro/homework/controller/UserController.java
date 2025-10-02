package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication);
    }

    @PostMapping("/set_password")
    public void setPassword(Authentication authentication, @RequestBody NewPassword newPassword) {
        userService.setPassword(authentication, newPassword.getNewPassword());
    }

    @PatchMapping("/me")
    public UpdateUser updateUser(Authentication authentication, @RequestBody UpdateUser updateUser) {
        return userService.updateUser(authentication, updateUser);
    }
}
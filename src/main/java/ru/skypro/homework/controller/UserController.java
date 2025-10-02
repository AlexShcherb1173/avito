package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication);
    }

    @PostMapping("/set_password")
    public void setPassword(Authentication authentication, @RequestBody NewPasswordDto newPassword) {
        userService.setPassword(authentication, newPassword.getNewPassword());
    }

    @PatchMapping("/me")
    public UpdateUserDto updateUser(Authentication authentication, @RequestBody UpdateUserDto updateUser) {
        return userService.updateUser(authentication, updateUser);
    }
}
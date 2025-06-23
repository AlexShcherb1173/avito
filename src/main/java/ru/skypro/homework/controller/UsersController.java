package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

@Slf4j
@RestController
@RequestMapping("users")
public class UsersController {

    @PostMapping("set_password")
    public ResponseEntity<?> setPassword(NewPassword newPassword) {
        return ResponseEntity.ok(newPassword);
    }

    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(new UserDto());
    }

    @PatchMapping("me")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping("me/image")
    public ResponseEntity<?> updateImage(@RequestBody UserDto user) {
        return ResponseEntity.ok(user);
    }

}

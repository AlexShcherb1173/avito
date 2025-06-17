package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@RestController
@RequestMapping("users")
public class UsersController {

    @PostMapping("set_password")
    public ResponseEntity<?> setPassword(NewPassword newPassword) {
        return ResponseEntity.ok(newPassword);
    }

    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(new User());
    }

    @PatchMapping("me")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping("me/image")
    public ResponseEntity<?> updateImage(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }

}

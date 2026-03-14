package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.service.AuthService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .map(userService::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser,
                                           Authentication authentication) {
        return userService.findByEmail(authentication.getName())
                .flatMap(user -> userService.updateUser(user.getId(), updateUser))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword,
                                            Authentication authentication) {
        boolean changed = authService.changePassword(
                authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );

        if (changed) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
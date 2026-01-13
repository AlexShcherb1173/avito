package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("Получение информации о текущем пользователе");
        User user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUser updateUser,
                                           Authentication authentication) {
        log.info("Обновление информации пользователя");
        User updatedUser = userService.updateUser(authentication.getName(), updateUser, authentication);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image,
                                             Authentication authentication) throws IOException {
        log.info("Обновление аватара пользователя");
        userService.updateUserImage(authentication.getName(), image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/image")
    public ResponseEntity<byte[]> getUserImage(Authentication authentication) throws IOException {
        log.info("Получение аватара пользователя");
        byte[] imageBytes = userService.getUserImage(authentication.getName());
        return ResponseEntity.ok(imageBytes);
    }
}
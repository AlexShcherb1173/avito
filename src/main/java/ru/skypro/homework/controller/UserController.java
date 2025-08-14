package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(
            @RequestBody NewPassword newPassword,
            Authentication authentication) {
        if (userService.setPassword(newPassword, authentication)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(
            @RequestBody UpdateUser updateUser,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserInfo(updateUser));
    }

    @GetMapping(value = "/image/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getUserImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads", "users", filename);
        byte[] imageBytes = Files.readAllBytes(path);
        return ResponseEntity.ok().body(imageBytes);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUserImage(
            @RequestParam("image") MultipartFile imageFile,
            Authentication authentication) throws IOException {
        return ResponseEntity.ok(userService.setUserImage(imageFile));
    }
}
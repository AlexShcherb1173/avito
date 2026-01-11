package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getMe(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateMe(@RequestBody UpdateUser updateUser,
                                         Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateUserByEmail(email, updateUser));
    }

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword,
                                            Authentication authentication) {
        String email = authentication.getName();
        userService.setPassword(email, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/me/image", consumes = "multipart/form-data", produces = "application/octet-stream")
    public ResponseEntity<byte[]> updateImage(@RequestParam("image") MultipartFile image,
                                              Authentication authentication) {
        String email = authentication.getName();

        String imagePath = (image != null && image.getOriginalFilename() != null)
                ? image.getOriginalFilename()
                : null;

        return ResponseEntity.ok(userService.updateUserImage(email, imagePath));
    }
}

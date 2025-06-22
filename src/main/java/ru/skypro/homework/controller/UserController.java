package ru.skypro.homework.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PasswordChangeDTO;
import ru.skypro.homework.dto.UserResponseDTO;
import ru.skypro.homework.dto.UserUpdateDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody PasswordChangeDTO request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(new UserResponseDTO());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateDTO> updateUser(@RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(request);
    }

    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}

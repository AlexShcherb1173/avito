package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Получение данных текущего пользователя
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserDto(user));
    }

    // Обновление данных пользователя
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody @Valid UpdateUser dto,
            @AuthenticationPrincipal User user) {
        UserDto updated = userService.updateUser(user, dto);
        return ResponseEntity.ok(updated);
    }

    // Обновление аватара пользователя
    @PatchMapping("/me/image")
    public ResponseEntity<?> updateUserImage(
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = userService.updateUserImage(user, image);
        return ResponseEntity.ok().build();
    }

    // Смена пароля
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(
            @RequestBody @Valid NewPassword dto,
            @AuthenticationPrincipal User user) {
        userService.setPassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> setPassword(@RequestBody NewPasswordRequest passwordData){

        return ResponseEntity.ok().build();

    }

    @GetMapping("/me")
    @Operation(summary = "Получить данные пользователя")
    public UserDto getUser() {
        return new UserDto();
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление данных пользователя")
    public UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUser) {
        return new UpdateUserDto();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара пользователя")
    public ResponseEntity<?> updateUserImage(@RequestParam("image")MultipartFile image){

        return ResponseEntity.ok().build();

    }

}

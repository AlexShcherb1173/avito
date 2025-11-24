package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users") // Базовый путь для всех методов контроллера
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {


    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {
        // TODO: реализовать смену пароля
        return ResponseEntity.ok().build(); // Пока возвращаем 200 OK
    }

    @GetMapping("/me")// GET запрос для получения информации о текущем пользователе
    @Operation (summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(new User()); // Пока возвращаем пустого пользователя
    }

    @PatchMapping("/me") // PATCH для частичного обновления
    @Operation (summary = "Обновление информации об авторизованном пользователе" )
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(new UpdateUser()); // Пока возвращаем пустой объект
    }

    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    @Operation (summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        // @RequestParam - получаем файл из формы
        // consumes = "multipart/form-data" - принимаем multipart данные
        return ResponseEntity.ok().build();
    }

}

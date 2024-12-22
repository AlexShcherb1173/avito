package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserApiDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.CustomUserDetailsManager;

import java.io.IOException;
/**
 * Контроллер для работы с пользователями.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final CustomUserDetailsManager userService;
    /**
     * Конструктор контроллера UserController.
     */
    public UserController(CustomUserDetailsManager userService) {
        this.userService = userService;
    }
    /**
     * Обновление пароля.
     */
    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public void setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        log.info("Пользователь обновил пароль {}", userService.getCurrentUser().getUsername());
        userService.changePassword(newPasswordDto);
    }
    /**
     * Получение информации об авторизованном пользователе.
     */
    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public UserApiDto getUser() {
        return UserMapper.toApi(userService.getCurrentUser());
    }
    /**
     * Обновление информации об авторизованном пользователе.
     */
    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUserDto) {
        log.info("Обновление информации об авторизованном пользователе");
        return userService.updateUser(updateUserDto);
    }
    /**
     * Обновление аватара авторизованного пользователя.
     */
    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public void updateUserAvatar(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("Обновление аватара авторизованного пользователя");
        userService.updateUserAvatar(image);
    }
}

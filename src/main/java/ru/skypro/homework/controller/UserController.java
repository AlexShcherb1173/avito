package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

/**
 * REST-контроллер для обработки HTTP-запросов, связанных с пользователями.
 * Предоставляет API для управления профилем пользователя и смены пароля.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получает информацию о текущем аутентифицированном пользователе.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @return DTO с информацией о пользователе
     */
    @GetMapping("/me")
    public UserDto getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication);
    }

    /**
     * Изменяет пароль текущего аутентифицированного пользователя.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param newPassword DTO с новым паролем
     */
    @PostMapping("/set_password")
    public void setPassword(Authentication authentication, @RequestBody NewPasswordDto newPassword) {
        userService.setPassword(authentication, newPassword.getNewPassword());
    }

    /**
     * Обновляет информацию о текущем аутентифицированном пользователе.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param updateUser DTO с обновленными данными пользователя
     * @return DTO с обновленной информацией о пользователе
     */
    @PatchMapping("/me")
    public UpdateUserDto updateUser(Authentication authentication, @RequestBody UpdateUserDto updateUser) {
        return userService.updateUser(authentication, updateUser);
    }
}
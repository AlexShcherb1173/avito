package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

/**
 * Контроллер для управления данными пользователя.
 * Включает получение и обновление профиля, смену пароля и загрузку аватара.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UsersController {

    private final UserService userService;

    /**
     * Обновление пароля пользователя.
     *
     * @param passwordDto     DTO с текущим и новым паролем
     * @param authentication  объект аутентификации, содержащий логин пользователя
     */
    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword passwordDto,
                            Authentication authentication) {
        userService.updatePassword(authentication.getName(), passwordDto);
    }

    /**
     * Получение профиля текущего авторизованного пользователя.
     *
     * @param authentication объект аутентификации
     * @return DTO с информацией о пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public User getUserInfo(Authentication authentication) {
        return userService.getUserInfo(authentication.getName());
    }

    /**
     * Обновление информации профиля текущего пользователя.
     *
     * @param updateUser     DTO с обновляемыми данными
     * @param authentication объект аутентификации
     * @return DTO с обновлённой информацией
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser,
                                 Authentication authentication) {
        return userService.updateUser(authentication.getName(), updateUser);
    }

    /**
     * Загрузка нового аватара пользователя.
     *
     * @param image          изображение в формате multipart/form-data
     * @param authentication объект аутентификации
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam MultipartFile image,
                                Authentication authentication) {
        userService.updateUserImage(authentication.getName(), image);
    }
}

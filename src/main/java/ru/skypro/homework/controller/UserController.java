package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

/**
 * Контроллер для управления пользователями.
 * Этот контроллер предоставляет API для работы с пользователями, включая обновление пароля,
 * получение информации о текущем пользователе, обновление данных пользователя и изменение аватара.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserController {

    private final UserService userService;

    /**
     * Обновление пароля авторизованного пользователя.
     *
     * @param password DTO с новым паролем.
     * @return {@link ResponseEntity} со статусом 200 (OK) при успешном обновлении.
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "Пароль успешно обновлен")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPasswordDTO password) {
        userService.updatePassword(password);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение информации о текущем авторизованном пользователе.
     *
     * @return {@link ResponseEntity} с {@link UserDTO}, содержащим данные пользователя.
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "Данные пользователя успешно получены")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthUser() {
        UserDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Обновление данных авторизованного пользователя.
     *
     * @param updateUser DTO с обновленной информацией о пользователе.
     * @return {@link ResponseEntity} с обновленным {@link UserDTO}.
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserDTO updateUser) {
        UserDTO updatedUser = userService.updateUser(updateUser);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Обновление аватара авторизованного пользователя.
     *
     * @param image Файл изображения для нового аватара.
     * @return {@link ResponseEntity} с обновленным {@link UserDTO}.
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "Аватар успешно обновлен")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUserImage(@RequestParam("image") MultipartFile image) {
        UserDTO user = userService.updateUserImage(image);
        return ResponseEntity.ok(user);
    }
}

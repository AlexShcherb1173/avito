package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Пользователи")
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Методы для управления пользователями и их данными")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Сменить пароль", description = "Позволяет пользователю сменить пароль")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO, Authentication authentication) {
        userService.updatePassword(newPasswordDTO, authentication);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить информацию о пользователе", description = "Возвращает данные текущего авторизованного пользователя")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }

    @Operation(summary = "Обновить данные пользователя", description = "Позволяет изменить имя, фамилию или другие данные пользователя")
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO, Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(updateUserDTO, authentication));
    }

    @Operation(summary = "Обновить аватар пользователя", description = "Позволяет загрузить или заменить аватар пользователя")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserAvatar(@RequestPart("image") MultipartFile image, Authentication authentication) {
        userService.updateUserAvatar(image, authentication);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Получить аватар пользователя", description = "Позволяет получить аватар пользователя")
    @GetMapping(value = "/me/image/{id}/get")
    public void downloadAvatarFromFileSystem(@PathVariable int id, HttpServletResponse response)
            throws IOException {
        userService.downloadAvatarFromFileSystem(id, response);
    }
}

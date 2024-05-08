package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.rq.user.NewPassword;
import ru.skypro.homework.dto.rq.user.UpdateUser;
import ru.skypro.homework.dto.rs.user.User;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserServiceImpl;



@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обновление пароля
     * @param newPassword новый пароль
     * @return {@code ResponseEntity.ok(new NewPassword())} возвращает http статус
     */
    @PostMapping("/set_password") //изменение пароля
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        userService.setPassword(newPassword, authentication.getName());
        return ResponseEntity.ok().build();

    }

    /**
     * Получение информации об авторизованном пользователе
     * @return {@code ResponseEntity.ok(new User())} возвращает информацию о пользователе
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    /**
     * Обновление информации об авторизованном пользователе
     * @param updateUser объект содержащий обновленную информацию
     * @return {@code ResponseEntity.ok(new UpdateUser())} обновленную информацию о пользователе
     */
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }

    /**
     * Обновление аватара авторизованного пользователя
     * @param image новый аватар пользователя
     * @return {@code ResponseEntity.ok().build()} статус №200 или №401
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok().build();
    }

}

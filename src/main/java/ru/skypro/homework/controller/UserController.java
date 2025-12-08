package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

/**
 * REST контроллер для управления профилем пользователя.
 * Обрабатывает запросы связанные с получением и обновлением данных пользователя,
 * сменой пароля и управлением аватаром.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Изменяет пароль текущего аутентифицированного пользователя.
     *
     * @param newPassword объект с текущим и новым паролем
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity со статусом 200 OK при успешной смене пароля,
     *         или 403 Forbidden при неверном текущем пароле
     */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody @Valid NewPassword newPassword,
                                         Authentication authentication) {
        log.info("Changing password for user: {}", authentication.getName());
        try {
            userService.updatePassword(authentication.getName(),
                    newPassword.getCurrentPassword(),
                    newPassword.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Получает информацию о текущем аутентифицированном пользователе.
     *
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с объектом {@link User} и статусом 200 OK,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        log.info("Getting current user: {}", authentication.getName());
        try {
            User user = userService.getCurrentUser(authentication.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param updateUser объект с обновляемыми данными пользователя
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с обновленным объектом {@link UpdateUser} и статусом 200 OK,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody @Valid UpdateUser updateUser,
                                                 Authentication authentication) {
        log.info("Updating user: {}", authentication.getName());
        try {
            User updatedUser = userService.updateUser(authentication.getName(), updateUser);
            // Возвращаем UpdateUser вместо User согласно спецификации
            UpdateUser response = new UpdateUser();
            response.setFirstName(updatedUser.getFirstName());
            response.setLastName(updatedUser.getLastName());
            response.setPhone(updatedUser.getPhone());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновляет аватар текущего пользователя.
     *
     * @param image файл изображения для загрузки
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity со статусом 200 OK при успешной загрузке,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image,
                                             Authentication authentication) {
        log.info("Updating user image for: {}", authentication.getName());
        try {
            userService.updateUserImage(authentication.getName(), image);
            // Возвращаем пустой ответ со статусом OK
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating user image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
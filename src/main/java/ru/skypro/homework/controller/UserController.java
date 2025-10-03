package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.dto.NewPassword;

/**
 * Контроллер для управления пользователями.
 * Включает эндпоинты для изменения пароля пользователя.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Изменяет пароль пользователя.
     * Проверяет текущий пароль и обновляет его на новый.
     *
     * @param newPassword объект, содержащий текущий и новый пароли
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return объект с новым паролем
     */
    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        NewPassword resultPassword = new NewPassword();
        boolean passwordChanged = userService.changePassword(
                authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );
        if (passwordChanged) {
            resultPassword.setCurrentPassword(newPassword.getNewPassword());
        }
        return resultPassword;
    }
}

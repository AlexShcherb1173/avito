package ru.skypro.homework.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Получение данных пользователя (заглушка)",
            description = "Возвращает тестовые данные пользователя")
    public ResponseEntity<User> getCurrentUser() {
        // Создаём и заполняем заглушку пользователя
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("Тестовый");
        user.setLastName("Пользователь");
        user.setPhone("+79990001122");
        user.setRole("USER");
        user.setImage("/images/default-avatar.jpg");

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление данных")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        // Заглушка: просто возвращаем то, что получили
        return updateUser;
    }
}
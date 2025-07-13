package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO.UserProfileDTO;
import ru.skypro.homework.dto.UserDTO.UserUpdateDTO;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {
    /**
     * Заглушка для получения данных текущего пользователя
     * @return Профиль пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        UserProfileDTO user = new UserProfileDTO();
        user.setId(1L);
        user.setUsername("stubUser");
        user.setEmail("stubuser@example.com");
        user.setPhone("+79991234567");
        user.setAvatar("https://example.com/avatar1.jpg");
        user.setRegistrationDate(LocalDateTime.now().minusDays(10));
        return ResponseEntity.ok(user);
    }

    /**
     * Заглушка для обновления данных пользователя
     * @param updateDTO Новые данные пользователя
     * @return Обновленный профиль
     */
    @PatchMapping("/me")
    public ResponseEntity<UserProfileDTO> updateCurrentUser(@RequestBody UserUpdateDTO updateDTO) {
        UserProfileDTO user = new UserProfileDTO();
        user.setId(1L);
        user.setUsername(updateDTO.getUsername() != null ? updateDTO.getUsername() : "stubUser");
        user.setPhone(updateDTO.getPhone() != null ? updateDTO.getPhone() : "+79991234567");
        user.setAvatar("https://example.com/updated_avatar.jpg");
        return ResponseEntity.ok(user);
    }

    /**
     * Заглушка для загрузки аватарки
     * @param file Загружаемый файл изображения
     * @return URL загруженной аватарки
     */
    @PostMapping("/me/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        // В реальной реализации файл должен сохраняться на сервере или в облачном хранилище
        return ResponseEntity.ok("https://example.com/avatars/" + file.getOriginalFilename());
    }
}

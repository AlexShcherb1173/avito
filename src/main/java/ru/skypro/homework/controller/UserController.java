package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UserDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public ResponseEntity<String> reNewPassword() {

        return ResponseEntity.ok("Пароль успешно обновлен");
    }

    @GetMapping("/me")
    public UserDTO getAuthUser() {

        return null;
    }


    @PatchMapping("/me")
    public ResponseEntity<String> updateAuthUser() {

        return ResponseEntity.ok("Данные были успешно обновлены");
    }

    @PatchMapping("/me/image")
    public ResponseEntity<String> updateAvatarOfAuthUser() {

        return ResponseEntity.ok("Аватар пользователя был успешно обновлен");
    }

}

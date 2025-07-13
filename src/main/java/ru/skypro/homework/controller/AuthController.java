package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.authDTO.LoginRequest;
import ru.skypro.homework.dto.authDTO.LoginResponse;
import ru.skypro.homework.dto.authDTO.RegisterRequest;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    //Заглушка для входа пользователя
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // В реальной реализации здесь должна быть проверка учетных данных
        LoginResponse response = new LoginResponse();
        response.setAccess_token("stub_jwt_token_" + System.currentTimeMillis());
        response.setUsername(request.getLogin().contains("@")
                ? request.getLogin().split("@")[0]
                : request.getLogin());
        response.setAvatar("https://example.com/avatars/" + request.getLogin().hashCode() + ".jpg");
        return ResponseEntity.ok(response);
    }

    //Заглушка для регистрации
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        // В реальной реализации здесь должна быть валидация и сохранение пользователя
        LoginResponse response = new LoginResponse();
        response.setAccess_token("stub_jwt_token_new_user_" + System.currentTimeMillis());
        response.setUsername(request.getUsername());
        response.setAvatar("https://example.com/default_avatar.jpg");
        return ResponseEntity.ok(response);
    }
}

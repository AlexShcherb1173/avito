package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.service.impl.AuthServiceImpl;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Авторизация", description = "Методы для авторизации пользователя")
public class AuthController implements AuthControllerInterface {
    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/login")
    @Override
    public void login(@RequestBody LoginDto loginDto) {
        authServiceImpl.login(loginDto);
    }
}

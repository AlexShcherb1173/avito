package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import javax.validation.Valid;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Регистрация", description = "Методы для регистрации пользователя")
public class RegisterController implements RegisterControllerInterface {
    private final AuthServiceImpl authServiceImpl;

    public RegisterController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/register")
    @Override
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        authServiceImpl.register(registerDto);
    }
}


package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.service.impl.LoginServiceImpl;
/**
 * Контроллер для работы с авторизацией пользователя.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Авторизация")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final LoginServiceImpl authenticationProvider;
    /**
     * Конструктор контроллера LoginController.
     */
    public LoginController(LoginServiceImpl authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    /**
     * Авторизация пользователя.
     */
    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public void login(@RequestBody LoginDto loginDto) {
        log.info("Авторизация пользователя {}", loginDto.getUsername());
        authenticationProvider.login(loginDto);
    }
}

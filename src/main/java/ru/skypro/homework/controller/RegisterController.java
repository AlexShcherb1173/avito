package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.CustomUserDetailsManager;
/**
 * Контроллер для регистрации пользователя.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Регистрация")
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private final CustomUserDetailsManager userService;
    /**
     * Конструктор контроллера RegisterController.
     */
    public RegisterController(CustomUserDetailsManager userService) {
        this.userService = userService;
    }
    /**
     * Регистрация пользователя.
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public void register(@RequestBody RegisterDto registerDto) {
        userService.createUser(registerDto);
        log.info("Зарегистрирован новый пользователь {}", registerDto.getUsername());
    }
}

package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.AuthService;
import javax.validation.Valid;
/**
 * Контроллер для управления авторизацией и регистрацией пользователей.
 * <p>
 * Этот контроллер предоставляет API для выполнения операций входа и регистрации пользователей.
 * </p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Авторизация и регистрация пользователя")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AdsController.class);
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername(userDto.getEmail());
            registerDto.setPassword("defaultPassword");
            registerDto.setFirstName(userDto.getFirstName());
            registerDto.setLastName(userDto.getLastName());
            registerDto.setPhone(userDto.getPhone());
            registerDto.setRoleDto(userDto.getRoleDto());

            if (authService.register(registerDto)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно создан");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким email уже существует");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            if (authService.login(loginDto.getUsername(), loginDto.getPassword())) {
                return ResponseEntity.ok().body("Авторизация успешна");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при авторизации: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            if (authService.register(registerDto)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким email уже существует");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при регистрации: " + e.getMessage());
        }
    }
}
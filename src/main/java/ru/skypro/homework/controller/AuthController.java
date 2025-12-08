package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

/**
 * REST контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает запросы на вход в систему и регистрацию новых пользователей.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    /**
     * Выполняет аутентификацию пользователя.
     * Использует базовую HTTP аутентификацию.
     *
     * @param login объект с учетными данными пользователя
     * @return ResponseEntity со статусом 200 OK при успешной аутентификации,
     *         или 401 Unauthorized при неверных учетных данных
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login) {
        log.info("Login attempt for user: {}", login.getUsername());
        boolean success = authService.login(login.getUsername(), login.getPassword());
        return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register объект с данными для регистрации
     * @return ResponseEntity со статусом 201 Created при успешной регистрации,
     *         или 400 Bad Request если пользователь с таким email уже существует
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Register register) {
        log.info("Registration attempt for user: {}", register.getUsername());

        log.debug("CONTROLLER RECEIVED - username: {}, firstName: {}, lastName: {}, phone: {}",
                register.getUsername(), register.getFirstName(), register.getLastName(), register.getPhone());

        boolean success = authService.register(register);
        return success ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
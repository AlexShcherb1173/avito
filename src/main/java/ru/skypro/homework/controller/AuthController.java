package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * <b>Контроллер для обработки запросов, связанных с аутентификацией пользователей.</b> <p>
 *
 * <p>Класс {@link AuthController} предоставляет API для входа в систему и регистрации пользователей.</p>
 *
 * <p>Обрабатывает следующие запросы:</p>
 * <ul>
 *     <li><code>/login</code> - для аутентификации пользователя.</li>
 *     <li><code>/register</code> - для регистрации нового пользователя.</li>
 * </ul>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * <b>Метод для обработки запроса на аутентификацию пользователя. </b> <p>
     *
     * @param login Объект, содержащий данные для аутентификации (имя пользователя и пароль).
     * @return ResponseEntity с HTTP статусом 200 (OK) в случае успешной аутентификации,
     *         или статусом 401 (UNAUTHORIZED) в случае неудачи.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * <b>Метод для обработки запроса на регистрацию нового пользователя. </b> <p>
     *
     * @param register Объект, содержащий данные для регистрации нового пользователя.
     * @return ResponseEntity с HTTP статусом 201 (CREATED) в случае успешной регистрации,
     *         или статусом 400 (BAD REQUEST) в случае ошибки.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

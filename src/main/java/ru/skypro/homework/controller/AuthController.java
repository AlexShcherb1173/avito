package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.UsersRepository;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsersRepository userRepository;
    private final AppMapper appMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<?> login(@RequestBody Login login) {
        Users user = userRepository.findByEmail(login.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже существует")
            }
    )
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        Users user = appMapper.registerToUser(register);  // Изменено с registerToUserEntity на registerToUser
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.PasswordServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@Tag(name = " Пользователи ", description = " API для работы с пользователями ")
public class UserController {

    private final UserServiceImpl userService;
    private final PasswordServiceImpl passwordService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserServiceImpl userService, PasswordServiceImpl passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("Set password called by user: {}", authentication.getName());
        try {
            boolean success = passwordService.setPassword(newPassword, authentication);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (RuntimeException e) {
            log.error("Error setting password", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Получение информации о пользователе по ID", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        log.info("Получить информацию о пользователе: {}", userId);
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.error(" Пользователь не найден: {}", userId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "обновление информации об авторизированном пользователе по ID", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),

            @ApiResponse(responseCode = "401", description = "Unauthorized")
    }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateUser> updateUser(@PathVariable Integer userId, @RequestBody UpdateUser updateUser) {
        log.info(" Обновить информацию о пользователе: {}", userId);
        try {
            User updatedUser = userService.updateUser(updateUser, userId);
            return ResponseEntity.ok(updateUser);
        } catch (RuntimeException e) {
            log.error("Пользователь не найден: {}", userId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удаление пользователя по ID", responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        log.info("Удаление пользователя: {}", userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Пользователь не найден: {}", userId, e);
            return ResponseEntity.notFound().build();
        }
    }

}

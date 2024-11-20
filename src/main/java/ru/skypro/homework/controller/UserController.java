package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.service.impl.UserServiceImpl;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Обновление пароля", tags = {"Пользователи"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#userId, authentication.name))")
    @PostMapping(path = "/users/set_password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("Метод setPassword, класса UserController. Принят объект newPassword: {}", newPassword);
        if (newPassword == null || newPassword.getNewPassword() == null || newPassword.getCurrentPassword() == null) {
            return ResponseEntity.badRequest().body("Пароль не может быть пустым");
        }

        try {
            userService.updatePassword(newPassword, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (UnauthorizedException e) {
            log.warn("Неавторизованный доступ при обновлении пароля: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неавторизованный доступ");
        } catch (ForbiddenException e) {
            log.warn("Доступ запрещен при обновлении пароля: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен");
        } catch (Exception e) {
            log.error("Ошибка при обновлении пароля: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обновлении пароля");
        }
    }

    @Operation(summary = "Получение информации об авторизованном пользователе", tags = {"Пользователи"})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserModel.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<User> getUser() {
        log.info("Метод getUser, класса UserController.");
        try {
            User user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException e) {
            log.error("Ошибка авторизации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NotFoundException e) {
            log.error("Пользователь не найден: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Ошибка при получении информации о пользователе: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе", tags = {"Пользователи"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#userId, authentication.name))")
    @PatchMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> updateUser(@RequestBody User updateUser, Authentication authentication) {
        log.info("Метод updateUser, класса UserController. Принят объект updateUser: {}", updateUser.toString());
        userService.updateUser(updateUser, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя", tags = {"Пользователи"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#userId, authentication.name))")
    @PatchMapping(path = "/users/me/image", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "string", format = "binary"))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> updateUserImage(@RequestPart(value = "image", required = true) MultipartFile image) {
        log.info("Метод uploadUserImage, класса UserController. Принят файл image: {}", image.getOriginalFilename());
        UserModel currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован");
        }

        try {
            userService.updateUserImage(currentUser, image);
            log.info("Изображение успешно обновлено для пользователя: {}", currentUser);
            return ResponseEntity.ok().body("Изображение успешно обновлено");
        } catch (Exception e) {
            log.error("Ошибка при обновлении изображения для пользователя: {}", currentUser, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении изображения");
        }
    }
}

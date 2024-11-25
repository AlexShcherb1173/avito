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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Обновление пароля", tags = {"Пользователи"})
    @PostMapping(path = "/users/set_password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> updatePassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("Метод updatePassword, класса UserController. Принят объект newPassword: {}", newPassword.toString());
        if (newPassword != null) {
            userService.updatePassword(newPassword, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Получение информации об авторизованном пользователе", tags = {"Пользователи"})
    @GetMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = ""))
    })
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("Метод getUser, класса UserController.");
        User userDto = userService.getUser(authentication.getName());
        if (userDto != null) {
            log.info("Получен объект User {}", userDto);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser,
                                                 Authentication authentication) throws IOException {
        log.info("Вошли в метод updateUserAvatar контроллера UserController. Принят объект updateUserAvatar: {}", updateUser);
        if (authentication != null) {
            userService.updateUser(updateUser, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(path = "/users/me/image", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateUserImage(@RequestPart(value = "image", required = true) MultipartFile image,
                                             Authentication authentication) throws IOException {
        log.info("Вошли в метод uploadUserImage, класса UserController. Принят файл image: {}", image.toString());
        if (authentication != null) {
            userService.updateUserAvatar(image, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

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
import ru.skypro.homework.model.UserEntity;
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
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#userId, authentication.name))")
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
    //@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#userId, authentication.name))")
    @GetMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "Пользователь не авторизован"))
    })
    public ResponseEntity<UserEntity> getUser() {
        log.info("Метод getUser, класса UserController.");
//        try {
//            userService.getCurrentUser();
//            UserEntity user = userService.getCurrentUser();
//            return ResponseEntity.ok(user);
//        } catch (UnauthorizedException e) {
//            log.error("Ошибка авторизации: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        } catch (NotFoundException e) {
//            log.error("Пользователь не найден: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (Exception e) {
//            log.error("Ошибка при получении информации о пользователе: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "Пользователь не авторизован")),
    })
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser,
                                        Authentication authentication) {
        log.info("Вошли в метод updateUser контроллера UserController. Принят объект updateUser: {}", updateUser);
        userService.updateUser(updateUser, authentication.getName());
        return ResponseEntity.ok().build();
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
        userService.updateUserImage(image, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /*
    @Operation(summary = "Обновление информации об авторизованном пользователе", tags = {"Пользователи"})
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @userService.isOwner(#updateUser.id, authentication.name))")
    @PatchMapping(path = "/profile/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser .class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> updateUser (@RequestBody User updateUser , Authentication authentication) {
        log.info("Метод updateUser , класса UserController. Принят объект update: User {}", updateUser.toString());

        // Проверка авторизации пользователя
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован");
        }

        // Проверка, что пользователь существует
        if (updateUser  == null || !userService.isOwner(updateUser.getId(), authentication.getName())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        // Обновление пользователя
        try {
            userService.updateUser(updateUser , authentication.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при обновлении пользователя");
        }
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
        UserEntity currentUser = userService.getCurrentUser();
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

     */

//    @Operation(summary = "Обновление аватара авторизованного пользователя")
//    @PatchMapping(path = "/users/me/image", consumes = "multipart/form-data")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
//    })
//    public ResponseEntity<?> updateUserImage(@RequestPart(value = "image", required = true) MultipartFile image,
//                                             Authentication authentication) throws IOException {
//        log.info("Вошли в метод uploadUserImage, класса UserController. Принят файл image: {}", image.toString());
//        userService.updateUserImage(image, authentication.getName());
//        return ResponseEntity.ok().build();
//
//    }
}

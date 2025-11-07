package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для работы с пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "Пароль успешно изменен")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPasswordDto newPasswordDto, Authentication authentication) {
        try {
            boolean success = userService.updatePassword(newPasswordDto, authentication.getName());
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error("Error updating password for user:{}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    ResponseEntity<UserDto> getUser(Authentication authentication) {
        try {
            UserDto userDto = userService.getUser(authentication.getName());
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            log.error("Error getting user info for: {}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "обновление информации об авторизованном пользователе (имя, фамилия, телефон)")
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto, Authentication authentication) {
        try {
            UpdateUserDto updatedUser = userService.updateUser(updateUserDto, authentication.getName());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error  updating user info for: {}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "загрузка/ обновление аватара авторизованного пользователя")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image
            , Authentication authentication) {
        try {
            boolean success = userService.updateUserImage(image, authentication.getName());
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid image for user: {}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            log.error("Error saving image for user: {}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File error");
        } catch (Exception e) {
            log.error("Error updating user image for: {}", authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

    @Operation(summary = "Просмотр аватара по имени пользователя")
    @GetMapping("/{username}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String username) {
        try {
            byte[] imageBytes = userService.getUserImage(username);
            String contentType = userService.getUserImageContentType(username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setCacheControl("max-age=60");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error loading image for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Просмотр аватара авторизовнного пользователя")
    @GetMapping("/me/image")
    public ResponseEntity<byte[]> getMyImage(Authentication authentication) {
        try{
            String username = authentication.getName();
            byte[] imageBytes = userService.getUserImage(authentication.getName());
            String contentType = userService.getUserImageContentType(username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setCacheControl("max-age=3600");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error loading image for current user", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}

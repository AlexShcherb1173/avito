package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword) {
        log.info("Updating password");
        // TODO: Implement in service layer
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUser updateUser) {
        log.info("Updating user info");
        // TODO: Implement in service layer
        User user = new User();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        log.info("Getting current user info");
        // TODO: Implement in service layer
        User user = new User();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) {
        log.info("Updating user image");
        // TODO: Implement in service layer
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
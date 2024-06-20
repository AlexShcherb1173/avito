package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "403")
    })
    public ResponseEntity<Void> setPassword(@RequestBody(required = false) NewPassword newPassword) {
        log.info("Method setPassword() in UserController is used");
        if (userService.setPassword(newPassword)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content)
    })
    public ResponseEntity<UserDto> getUser() {
        log.info("Method getUser() in UserController is used");
        UserDto user = userService.getUser();
        if (user!= null) {
            return ResponseEntity.ok(user);
        } return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content)
    })
    public ResponseEntity<UpdateUser> updateUser(@RequestBody(required = false) UpdateUser user) {
        log.info("Method updateUser() in UserController is used");
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image) {
        log.info("Method updateUserImage() in UserController is used");
        try {
            userService.updateUserImage(image);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}

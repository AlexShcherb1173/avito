package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = " Пользователи ", description = " API для работы с пользователями ")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "обновление пароля", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),

            @ApiResponse(responseCode = "401",description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?>
    setPassword(@RequestBody NewPassword newPassword){
        log.info(" Установите новый пароль ");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "получение информации об авторизованном пользователе", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401",description = "Unauthorized")
    }
    )
    @GetMapping("/me")
    public ResponseEntity<User> getUser(){
        log.info(" Получить пользователя по имени");
        User user = new User();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "обновление информации об авторизированном пользователе", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),

            @ApiResponse(responseCode = "401",description = "Unauthorized")
    }
    )
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser){
        log.info(" Обновить пользователя по имени ");
        return ResponseEntity.ok(updateUser);
    }

}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Авторизация пользователя")
public class Login {

    @Schema(description = "логин", example = "user@gmail.com")
    private String username;

    @Schema(description = "пароль", example = "password123")
    private String password;
}
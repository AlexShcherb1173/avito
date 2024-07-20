package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDto {

    @Schema(description = "логин",
            required = true,
            minLength = 8,
            maxLength = 16)
    private String username;

    @Schema(description = "пароль",
            required = true,
            minLength = 4,
            maxLength = 32)
    private String password;
}

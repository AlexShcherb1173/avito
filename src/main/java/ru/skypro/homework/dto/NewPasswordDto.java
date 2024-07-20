package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewPasswordDto {

    @Schema(description = "Текущий пароль",
            required = true,
            minLength = 8,
            maxLength = 16)
    private String currentPassword;

    @Schema(description = "Новый пароль",
            required = true,
            minLength = 8,
            maxLength = 16)
    private String newPassword;
}

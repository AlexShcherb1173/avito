package ru.skypro.homework.dto.Registration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для смены пароля пользователя")
public class Password {

    @Schema(minLength = 8,
            maxLength = 16,
            description = "Текущий пароль",
            example = "password")
    private String currentPassword;

    @Schema(minLength = 8,
            maxLength = 16,
            description = "Новый пароль",
            example = "password1")
    private String newPassword;
}


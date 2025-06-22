package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
/**
 * Задание И смена паролЯ
 */
@Schema(description = "Данные для смены пароля пользователя")
public class Password {

    @Schema(description = "Текущий пароль",
            example = "oldPassword123")
    private String currentPassword;

    @Schema(description = "Новый пароль",
            example = "newPassword456")
    private String newPassword;

}


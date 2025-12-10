package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO для смены пароля пользователя.
 * Содержит текущий и новый пароль для проверки и обновления.
 */
@Data
@Schema(description = "Обновление пароля")
public class NewPassword {

    @NotBlank
    @Size(min = 8, max = 16)
    @Schema(description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 16)
    @Schema(description = "новый пароль", minLength = 8, maxLength = 16)
    private String newPassword;
}
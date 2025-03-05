package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class NewPasswordDTO {

    @Schema(description = "Текущий пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Текущий пароль должен содержать от 8 до 16 символов")
    private String currentPassword;

    @Schema(description = "Новый пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Новый пароль должен содержать от 8 до 16 символов")
    private String newPassword;
}

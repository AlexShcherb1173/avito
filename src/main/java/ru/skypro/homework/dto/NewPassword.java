package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NewPassword {

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

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Изменение пароля")
public class NewPassword {

    @Schema(description = "текущий пароль", example = "oldPassword123")
    private String currentPassword;

    @Schema(description = "новый пароль", example = "newPassword123")
    private String newPassword;
}
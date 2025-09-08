package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на смену пароля")
public class NewPassword {

    @Schema(description = "Текущий пароль", example = "OldP@ssw0rd", required = true)
    private String currentPassword;

    @Schema(description = "Новый пароль", example = "NewP@ssw0rd", required = true)
    private String newPassword;
}

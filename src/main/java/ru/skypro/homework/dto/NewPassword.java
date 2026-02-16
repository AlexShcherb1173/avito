package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NewPassword {
    @Schema(minLength = 8, maxLength = 16, description = "текущий пароль")
    private String currentPassword;
    @Schema(minLength = 8, maxLength = 16, description = "новый пароль")
    private String newPassword;
}

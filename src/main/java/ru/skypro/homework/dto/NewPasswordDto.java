package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class NewPasswordDto {

    @Schema(description = "текущий пароль")
    @Size(min = 8, max = 16)
    private String currentPassword;

    @Schema(description = "новый пароль")
    @Size(min = 8, max = 16)
    private String newPassword;

    public NewPasswordDto currentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }
}


package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для смены пароля пользователя.
 * Содержит новый пароль для установки.
 */
@Schema(description = "Request for password change")
public class NewPasswordDto {
    @Schema(description = "New password")
    private String newPassword;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
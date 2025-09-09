package ru.skypro.homework.dto;

/**
 * отвечает за передачу данных при смене пароля пользователя в приложении.
 */


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NewPassword {
    @NotBlank
    @Size(min = 8, max = 16)
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;

    public @NotBlank @Size(min = 8, max = 16) String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(@NotBlank @Size(min = 8, max = 16) String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public @NotBlank @Size(min = 8, max = 16) String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank @Size(min = 8, max = 16) String newPassword) {
        this.newPassword = newPassword;
    }
}
package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class NewPassword {
    private String currentPassword;
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}

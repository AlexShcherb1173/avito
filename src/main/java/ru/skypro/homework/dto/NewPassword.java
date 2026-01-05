package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class NewPassword {
    private String currentPassword;
    private String newPassword;


    public NewPassword() {
    }

    // Геттеры и сеттеры
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

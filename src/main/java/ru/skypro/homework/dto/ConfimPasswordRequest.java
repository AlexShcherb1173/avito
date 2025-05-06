package ru.skypro.homework.dto;

import org.apache.logging.log4j.util.Strings;

public class ConfimPasswordRequest {
    Strings currentPassword;
    Strings newPassword;

    public ConfimPasswordRequest(Strings currentPassword, Strings newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public ConfimPasswordRequest() {
    }

    public Strings getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(Strings currentPassword) {
        this.currentPassword = currentPassword;
    }

    public Strings getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(Strings newPassword) {
        this.newPassword = newPassword;
    }
}


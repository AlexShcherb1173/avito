package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.homework.service.impl.CheckService;

import java.util.Objects;
/**
 * DTO (Data Transfer Object) для создания нового пароля.
 * <p>
 * В классе представлены данные для создания нового пароля, а именно: текущий и новый пароли.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
public class NewPasswordDto {

    @Schema(type = "string",
            description = "текущий пароль",
            minLength = 8,
            maxLength = 16)
    private String currentPassword;

    @Schema(type = "string",
            description = "новый пароль",
            minLength = 8,
            maxLength = 16)
    private String newPassword;

    @Override
    public String toString() {
        return "NewPassword{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPasswordDto that = (NewPasswordDto) o;
        return Objects.equals(currentPassword, that.currentPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPassword, newPassword);
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        if (CheckService.checkLength(currentPassword, 8, 16)) {
            this.currentPassword = currentPassword;
        } else {
            throw new IllegalArgumentException("Длина пароля от 8 до 16 символов");
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        if (CheckService.checkLength(newPassword, 8, 16)) {
            this.newPassword = newPassword;
        } else {
            throw new IllegalArgumentException("Длина пароля от 8 до 16 символов");
        }
    }

    public NewPasswordDto() {
    }

    public NewPasswordDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}

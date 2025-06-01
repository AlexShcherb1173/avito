package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class NewPassword {

    private String currentPassword; // Текущий пароль
    private String newPassword; // Новый пароль

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NewPassword that = (NewPassword) o;
        return Objects.equals(currentPassword, that.currentPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPassword, newPassword);
    }

    @Override
    public String toString() {
        return "NewPassword{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}

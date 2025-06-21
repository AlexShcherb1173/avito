package ru.skypro.homework.dto.register;

import lombok.Data;

/**
 * DTO для изменения пароля пользователя
 */
@Data
public class NewPassword {
    private String currentPassword;
    private String newPassword;
}

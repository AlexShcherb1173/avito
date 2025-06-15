package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class NewPassword {

    private String currentPassword; // Текущий пароль
    private String newPassword;     // Новый пароль

}

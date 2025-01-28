package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class NewPassword {
    /*
    текущий пароль
     */
    @Size(min = 8, max = 16)
    private String currentPassword;
    /*
    новый пароль
     */
    @Size(min = 8, max = 16)
    private String newPassword;
}

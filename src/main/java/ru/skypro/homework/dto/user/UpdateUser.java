package ru.skypro.homework.dto.user;

import lombok.Data;

/**
 * DTO для обновления данных пользователя
 */
@Data
public class UpdateUser {
    private String firstName;
    private String lastName;
    private String phone;
}

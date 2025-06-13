package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UpdateUser {

    private String firstName; // Имя пользователя
    private String lastName;  // Фамилия пользователя
    private String phone;     // Телефон пользователя

    public UpdateUser() {}

}

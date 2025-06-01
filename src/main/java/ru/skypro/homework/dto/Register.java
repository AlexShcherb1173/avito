package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Register {

    private String username; // Логин
    private String password; // Пароль
    private String firstName; // Имя пользователя
    private String lastName; // Фамилия пользователя
    private String phone; // Телефон пользователя
    private Role role; // Роль пользователя
}

package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {

    private Integer id;         // id пользователя
    private String email;       // Логин пользователя
    private String firstName;   // Имя пользователя
    private String lastName;    // Фамилия пользователя
    private String phone;       // Телефон пользователя
    private Role role;          // Роль пользователя
    private String image;       // Ссылка на аватар пользователя

    public User() {

    }
}

package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class User {
    private int id;
    private String email;       //логин пользователя
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String image;       //ссылка на аватар пользователя
}

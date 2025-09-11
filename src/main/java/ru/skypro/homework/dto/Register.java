package ru.skypro.homework.dto;

/**
 * отвечает за получение и валидацию данных при регистрации нового пользователя
 */


import lombok.Data;

@Data
public class Register {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
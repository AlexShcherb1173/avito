package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class RegisterDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    // Какой тип ставить? В Апишке просмотреть!!! Тип указан Стринг но потом указан enum
    private String role;
    //private Role role;
}

package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Register {
    private String username;      // Логин (4-32 символа)
    private String password;      // Пароль (8-16 символов)
    private String firstName;     // Имя (2-16 символов)
    private String lastName;      // Фамилия (2-16 символов)
    private String phone;         // Формат +7 (XXX) XXX-XX-XX
    private Role role;            // Enum: USER, ADMIN
}

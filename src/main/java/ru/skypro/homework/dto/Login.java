package ru.skypro.homework.dto;

/**
 *  используется для передачи данных от фронтенда к бэкенду при попытке входа пользователя в систему
 */


import lombok.Data;

@Data
public class Login {
    private String username;
    private String password;
}
package ru.skypro.homework.dto;

/**
 *  используется для передачи данных от фронтенда к бэкенду при попытке входа пользователя в систему
 */


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Login {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    public @NotBlank @Size(min = 4, max = 32) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 4, max = 32) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 8, max = 16) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 16) String password) {
        this.password = password;
    }
}
package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для аутентификации пользователя.
 * Содержит учетные данные для входа в систему.
 */
@Schema(description = "Login data")
public class LoginDto {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "Password")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
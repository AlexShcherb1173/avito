package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Login {

    @Schema(description = "логин",
            required = true,
            minLength = 8,
            maxLength = 16)
    private String username;

    @Schema(description = "пароль",
            required = true,
            minLength = 4,
            maxLength = 32)
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

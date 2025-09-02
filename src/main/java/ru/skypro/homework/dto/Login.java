package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Авторизация пользователя")
public class Login {

    @Schema(description = "логин", minLength = 4, maxLength = 30)
    private String username;

    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

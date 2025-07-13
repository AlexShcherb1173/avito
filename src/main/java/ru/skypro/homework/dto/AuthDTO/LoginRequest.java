package ru.skypro.homework.dto.AuthDTO;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса авторизации пользователя
 */
public class LoginRequest {
    @NotBlank(message = "Логин не может быть пустым")
    private String login; // Логин (email или username)

    @NotBlank(message = "Пароль не может быть пустым")
    private String password; // Пароль пользователя

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

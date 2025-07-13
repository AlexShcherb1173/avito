package ru.skypro.homework.dto.AuthDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса регистрации нового пользователя
 */
public class RegisterRequest {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username; // Отображаемое имя

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String login; // Email пользователя

    @NotBlank(message = "Пароль не может быть пустым")
    private String password; // Пароль для входа


    public RegisterRequest(String username, String login, String password) {
        this.username = username;
        this.login = login;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

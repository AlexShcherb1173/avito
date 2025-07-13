package ru.skypro.homework.dto.authDTO;

/**
 * DTO для ответа после успешной авторизации/регистрации
 */
public class LoginResponse {
    private String access_token; // JWT токен для аутентификации
    private String username; // Отображаемое имя пользователя
    private String avatar; // URL аватарки пользователя
    public LoginResponse(String access_token, String username, String avatar) {
        this.access_token = access_token;
        this.username = username;
        this.avatar = avatar;
    }

    public LoginResponse() {

    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

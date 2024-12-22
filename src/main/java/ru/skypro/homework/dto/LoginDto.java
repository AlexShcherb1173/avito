package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.homework.service.impl.CheckService;

import java.util.Objects;
/**
 * DTO (Data Transfer Object) для данных авторизации пользователя.
 * <p>
 * В классе представлены данные для авторизации пользователя, а именно: логин и пароль.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
public class LoginDto {

    @Schema(type = "string",
            description = "пароль",
            minLength = 8,
            maxLength = 16)
    private String password;

    @Schema(type = "string",
            description = "логин",
            minLength = 4,
            maxLength = 32)
    private String username;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public LoginDto(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public LoginDto() {
    }

    @Override
    public String toString() {
        return "Login{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(password, loginDto.password) && Objects.equals(username, loginDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username);
    }

    public void setPassword(String password) {
        if (CheckService.checkLength(password, 8, 16)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Длина пароля от 8 до 16 символов");
        }
    }

    public void setUsername(String username) {
        if (CheckService.checkLength(username, 4, 32) && CheckService.checkUsername(username)) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Длина логина от 4 до 32 символов в формате \"yourmail@mail.ru\"");
        }
    }
}

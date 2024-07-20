package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterDto {

    @Schema(description = "логин",
            required = true,
            minLength = 3,
            maxLength = 32)
    private String username;

    @Schema(description = "пароль",
            required = true,
            minLength = 8,
            maxLength = 16)
    private String password;

    @Schema(description = "имя пользователя",
            required = true,
            minLength = 2,
            maxLength = 16)
    private String firstName;

    @Schema(description = "фамилия пользователя",
            required = true,
            minLength = 2,
            maxLength = 16)
    private String lastName;

    @Schema(description = "телефон пользователя",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "роль пользователя",
            allowableValues = {"USER", "ADMIN"})
    private RoleDto role;
}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @Schema(description = "Логин", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @Schema(description = "Пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

    @Schema(description = "Имя пользователя", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "Имя должно содержать от 2 до 16 символов")
    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    @Schema(description = "Фамилия пользователя", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "Фамилия должна содержать от 2 до 16 символов")
    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @Schema(description = "Телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "Роль пользователя", example = "USER")
    private RoleDTO role;
}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {
    @Schema(description = "логин")
    @Size(min = 4, max = 32)
    private String username;

    @Schema(description = "пароль")
    @Size(min = 8, max = 16)
    private String password;

    @Schema(description = "имя пользователя")
    @Size(min = 2, max = 16)
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @Size(min = 2, max = 16)
    private String lastName;

    @Schema(description = "телефон пользователя")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;


    @Schema(description = "роль пользователя")
    private Role role;

}

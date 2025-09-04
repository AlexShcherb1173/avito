package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для регистрации")
public class Register {

    @Schema(description = "Имя пользователя (логин/email)", example = "user@example.com", required = true)
    private String username;

    @Schema(description = "Пароль", example = "P@ssw0rd", required = true)
    private String password;

    @Schema(description = "Имя", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Телефон", example = "+7 900 123-45-67", required = true)
    private String phone;

    @Schema(description = "Роль пользователя", required = true)
    private Role role;
}

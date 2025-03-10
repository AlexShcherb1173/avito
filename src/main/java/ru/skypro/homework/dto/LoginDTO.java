package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для данных входа пользователя.
 * Включает информацию о логине и пароле пользователя.
 */
@Data
public class LoginDTO {

    @Schema(description = "Логин", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @Schema(description = "Пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
}

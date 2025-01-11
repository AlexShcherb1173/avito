package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий DTO для изменения пароля пользователя.
 * Используется для передачи текущего и нового пароля.
 */
@Data
@NoArgsConstructor
public class NewPasswordDto {

    /**
     * Текущий пароль пользователя.
     * Должен содержать от 8 до 16 символов.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "текущий пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Текущий пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Текущий пароль обязателен")
    private String currentPassword;

    /**
     * Новый пароль пользователя.
     * Должен содержать от 8 до 16 символов.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "новый пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Новый пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Новый пароль обязателен")
    private String newPassword;
}
package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий DTO для обновления данных пользователя.
 * Используется для передачи данных при обновлении информации о пользователе.
 */
@Data
@NoArgsConstructor
public class UpdateUserDto {

    /**
     * Имя пользователя.
     * Должно содержать от 3 до 10 символов.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "имя пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Имя должно содержать от 3 до 10 символов")
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    /**
     * Фамилия пользователя.
     * Должна содержать от 3 до 10 символов.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "фамилия пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    /**
     * Номер телефона пользователя.
     * Должен соответствовать формату +7 (XXX) XXX-XX-XX.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Номер телефона должен соответствовать формату +7 (XXX) XXX-XX-XX")
    @NotBlank(message = "Телефон обязателен")
    private String phone;
}
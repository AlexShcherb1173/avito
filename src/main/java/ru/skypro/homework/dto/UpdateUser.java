package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO для обновления данных пользователя.
 * Используется при редактировании профиля пользователя.
 * Все поля обязательны для заполнения и проходят валидацию.
 */
@Data
@Schema(description = "Обновление данных пользователя")
public class UpdateUser {

    @NotBlank
    @Size(min = 3, max = 10)
    @Schema(description = "имя пользователя", minLength = 3, maxLength = 10)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 10)
    @Schema(description = "фамилия пользователя", minLength = 3, maxLength = 10)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Schema(description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
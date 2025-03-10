package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для обновления данных пользователя.
 * Включает информацию об имени, фамилии и телефоне пользователя.
 */
@Data
public class UpdateUserDTO {

    @Schema(description = "Имя пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Имя должно содержать от 3 до 10 символов")
    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    @Schema(description = "Фамилия пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @Schema(description = "Телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}

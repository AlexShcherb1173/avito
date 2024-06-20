package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUser {
    @Size(min = 3, max = 10)
    @Schema(description = "имя пользователя")
    private String firstName;
    @Size(min = 3, max = 10)
    @Schema(description = "фамилия пользователя")
    private String lastName;
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Schema(description = "телефон пользователя")
    private String phone;
}

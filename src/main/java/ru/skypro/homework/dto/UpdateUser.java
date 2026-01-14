package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для внесения изменений в данные пользователя")
public class UpdateUser {
    @Schema(
            type = "string",
            description = "имя пользователя"
    )
    @Size(min = 3, max = 10)
    private String firstName;

    @Schema(
            type = "string",
            description = "фамилия пользователя"
    )
    @Size(min = 3, max = 10)
    private String lastName;

    @Schema(
            type = "string",
            description = "телефон пользователя"
    )
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}

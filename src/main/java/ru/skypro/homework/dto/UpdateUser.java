package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@Validated
public class UpdateUser {

    @Schema(description = "имя пользователя")
    @Max(10)
    @Min(3)
    private String firstname;

    @Schema(description = "фамилия пользователя")
    @Max(10)
    @Min(3)
    private String lastname;

    @Schema(description = "телефон пользователя")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}

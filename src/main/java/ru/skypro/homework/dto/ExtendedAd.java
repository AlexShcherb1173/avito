package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ExtendedAd {
    @Schema(description = "имя автора объявления")
    @NotBlank
    private String authorFirstName;

    @Schema(description = "фамилия автора объявления")
    @NotBlank
    private String authorLastName;

    @Schema(description = "описание объявления")
    @NotBlank
    @Size(min = 8, max = 64)
    private String description;

    @Schema(description = "логин автора объявления")
    @Email
    private String email;

    @Schema(description = "телефон автора объявления")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}

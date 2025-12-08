package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для расширенной информации об объявлении.
 * Содержит полные данные об объявлении и информацию об авторе.
 */
@Data
@Schema(description = "Расширенные данные объявления")
public class ExtendedAd {

    @Schema(description = "id объявления")
    private Integer pk;

    @Schema(description = "имя автора объявления")
    private String authorFirstName;

    @Schema(description = "фамилия автора объявления")
    private String authorLastName;

    @Schema(description = "описание объявления")
    private String description;

    @Schema(description = "логин автора объявления")
    private String email;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "телефон автора объявления")
    private String phone;

    @Schema(description = "цена объявления")
    private Integer price;

    @Schema(description = "заголовок объявления")
    private String title;
}
package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Расширенная информация об объявлении")
public class ExtendedAd {

    @Schema(description = "id объявления", example = "10")
    private Integer pk;

    @Schema(description = "имя автора", example = "Иван")
    private String authorFirstName;

    @Schema(description = "фамилия автора", example = "Иванов")
    private String authorLastName;

    @Schema(description = "описание объявления", example = "Отличное состояние")
    private String description;

    @Schema(description = "email автора", example = "user@gmail.com")
    private String email;

    @Schema(description = "ссылка на картинку", example = "/images/ad.jpg")
    private String image;

    @Schema(description = "телефон автора", example = "+79991234567")
    private String phone;

    @Schema(description = "цена объявления", example = "15000")
    private Integer price;

    @Schema(description = "заголовок объявления", example = "Продам велосипед")
    private String title;
}
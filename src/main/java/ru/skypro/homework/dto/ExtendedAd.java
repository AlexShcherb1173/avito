package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Расширенная информация об объявлении")
public class ExtendedAd {

    @Schema(description = "Идентификатор объявления", example = "123", required = true)
    private Integer pk;

    @Schema(description = "Имя автора", example = "Иван")
    private String authorFirstName;

    @Schema(description = "Фамилия автора", example = "Иванов")
    private String authorLastName;

    @Schema(description = "Подробное описание", example = "Практически новый велосипед, катался мало")
    private String description;

    @Schema(description = "Email автора", example = "seller@example.com")
    private String email;

    @Schema(description = "URL изображения", example = "/images/ads/123.jpg")
    private String image;

    @Schema(description = "Телефон автора", example = "+7 900 123-45-67")
    private String phone;

    @Schema(description = "Цена", example = "15000", required = true)
    private Integer price;

    @Schema(description = "Заголовок", example = "Продам велосипед")
    private String title;
}


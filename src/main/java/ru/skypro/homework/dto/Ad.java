package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Краткая информация об объявлении")
public class Ad {

    @Schema(description = "Идентификатор автора", example = "42", required = true)
    private Integer author;

    @Schema(description = "URL изображения объявления", example = "/images/ads/123.jpg")
    private String image;

    @Schema(description = "Идентификатор объявления", example = "123", required = true)
    private Integer pk;

    @Schema(description = "Цена объявления в рублях", example = "15000", required = true)
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам велосипед", required = true)
    private String title;
}

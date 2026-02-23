package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Краткая информация об объявлении")
public class Ad {

    @Schema(description = "id автора объявления", example = "1")
    private Integer author;

    @Schema(description = "ссылка на картинку объявления", example = "/images/ad.jpg")
    private String image;

    @Schema(description = "id объявления", example = "10")
    private Integer pk;

    @Schema(description = "цена объявления", example = "15000")
    private Integer price;

    @Schema(description = "заголовок объявления", example = "Продам велосипед")
    private String title;
}
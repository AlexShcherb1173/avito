package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AdDto {

    @Schema(description = "id автора объявления",
            required = true)
    private int author;

    @Schema(description = "ссылка на картинку объявления",
            required = true)
    private String image;

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "цена объявления",
            required = true)
    private int price;

    @Schema(description = "заголовок объявления",
            required = true)
    private String title;
}

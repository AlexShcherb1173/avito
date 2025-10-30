package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных рекламного объявления")
public class Ad {
    @Schema(
            type = "integer",
            format = "int32",
            description = "id автора объявления"
    )
    private Integer author;

    @Schema(
            type = "string",
            description = "ссылка на картинку объявления"
    )
    private String image;

    @Schema(
            type = "integer",
            format = "int32",
            description = "id объявления"
    )
    private Integer pk;

    @Schema(
            type = "integer",
            format = "int32",
            description = "цена объявления"
    )
    private Integer price;

    @Schema(
            type = "string",
            description = "заголовок объявления"
    )
    private String title;
}

package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdResponse {
    /**
     * id объявления
     */
    @Schema(description = "id объявления")
    private Long pk;
    /**
     * id автора объявления
     */
    @Schema(description = "id автора объявления")
    private Long author;
    /**
     * ссылка на картинку объявления
     */
    @Schema(description = "ссылка на картинку объявления")
    private String image;
    /**
     * цена объявления
     */
    @Schema(description = "цена объявления")
    private Integer price;
    /**
     * заголовок объявления
     */
    @Schema(description = "заголовок объявления")
    private String title;
}

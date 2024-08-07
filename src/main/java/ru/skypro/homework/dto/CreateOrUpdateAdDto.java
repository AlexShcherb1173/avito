package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateOrUpdateAdDto {

    @Schema(description = "заголовок объявления",
            minLength = 4,
            maxLength = 32)
    private String title;

    @Schema(description = "описание объявления",
            minLength = 8,
            maxLength = 64)
    private String description;

    @Schema(description = "цена объявления",
            minimum = "0",
            maximum = "10000000")
    private int price;

}

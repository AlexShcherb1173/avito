package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateOrUpdateAdDto {

    @Schema(description = "заголовок объявления",
            required = true,
            minLength = 4,
            maxLength = 32)
    private String title;

    @Schema(description = "цена объявления",
            required = true,
            minimum = "0",
            maximum = "10000000")
    private int price;

    @Schema(description = "описание объявления",
            required = true,
            minLength = 8,
            maxLength = 64)
    private String description;
}

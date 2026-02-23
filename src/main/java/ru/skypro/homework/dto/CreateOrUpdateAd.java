package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Создание или обновление объявления")
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления", example = "Продам телефон")
    private String title;

    @Schema(description = "цена объявления", example = "20000")
    private Integer price;

    @Schema(description = "описание объявления", example = "Почти новый")
    private String description;
}
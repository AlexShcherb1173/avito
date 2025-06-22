package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
/**
 * класс для создания рекламного объявления
 */
@Schema(description = "Данные для создания или обновления объявления")
public class CreateOrUpdateAd {
    /**
     * Заголовок объявления
     */
    @Schema(description = "Заголовок объявления",
            minLength = 5,
            maxLength = 100)
    private String title = "";

    /**
     * Цена товара/услуги
     */
    @Schema(description = "Цена в рублях",
            minimum = "0")
    private Integer price = 0;

    /**
     * Описание объявления
     */
    @Schema(description = "Подробное описание",
            maxLength = 1000)
    private String description = "";
}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для создания или обновления объявления")
public class CreateOrUpdateAd {

    @Schema(description = "Заголовок объявления", example = "Продам велосипед", required = true)
    private String title;

    @Schema(description = "Цена объявления в рублях", example = "15000", required = true)
    private Integer price;

    @Schema(description = "Описание объявления", example = "Почти новый, использовался несколько раз", required = true)
    private String description;
}

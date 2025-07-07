package ru.skypro.homework.dto.Advertisement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для создания или обновления объявления")
public class CreateOrUpdateAd {

    @Schema(description = "Заголовок объявления",
            minLength = 5,
            maxLength = 100)
    private String title = "";


    @Schema(description = "Цена в рублях",
            minimum = "0")
    private Integer price = 0;


    @Schema(description = "Подробное описание",
            maxLength = 1000)
    private String description = "";

}

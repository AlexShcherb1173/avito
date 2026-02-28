package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для создания/обновления объявления")
@Data
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления (min 4, max 32)")
    private String title;

    @Schema(description = "цена объявления (от 0 до 10 млн)")
    private Integer price;

    @Schema(description = "описание объявления (min 8, max 64)")
    private String description;

}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdDTO {

    @Schema(description = "ID автора объявления")
    @NotNull(message = "ID автора не должен быть пустым")
    private int author;

    @Schema(description = "Ссылка на картинку объявления")
    private String image;

    @Schema(description = "ID объявления")
    @NotNull(message = "ID объявления не должен быть пустым")
    private int pk;

    @Schema(description = "Цена объявления")
    @NotNull(message = "Цена не должна быть пустой")
    private Integer price;

    @Schema(description = "Заголовок объявления")
    private String title;
}

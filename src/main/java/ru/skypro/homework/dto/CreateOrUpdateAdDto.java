package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAdDto {
    @Schema(description = "заголовок объявления")
    @Size(min = 4, max = 32,
            message = "Длина заголовка объявления должна быть не менее 4-х " +
                    "и не более 32-х символов")
    private String title;


    @Schema(description = "цена объявления")
    @Min(0)
    @Max(10000000)
    private Integer price;

    @Schema(description = "описание объявления")
    @Size(min = 8, max = 64,
            message = "Длина описания объявления должна быть не менее 8-ми " +
                    "и не более 64-х символов")
    private String description;
}

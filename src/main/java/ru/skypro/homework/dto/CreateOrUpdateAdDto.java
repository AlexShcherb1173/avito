package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAdDto {
    @Schema(description = "заголовок объявления")
    @Size(min = 4, max = 32)
    private String title;
    @Schema(description = "цена объявления")
    @PositiveOrZero
    @DecimalMax(value = "10000000")
    private int price;
    @Schema(description = "описание объявления")
    @Size(min = 8, max = 64)
    private String description;


}

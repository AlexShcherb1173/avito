package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAdDTO {
    @Size(min = 4, max = 32)
    @Schema(description = "заголовок объявления")
    private String title;
    @Max(10000000)
    @Min(0)
    @Schema(description = "цена объявления")
    private int price;
    @Size(min = 8, max = 64)
    @Schema(description = "описание объявления")
    private String description;
}

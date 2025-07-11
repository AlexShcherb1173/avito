package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Данные для создания/обновления объявления")
public class CreateOrUpdateAd {
    @Size(min = 4, max = 32)
    private String title;

    @Min(0) @Max(10000000)
    private Integer price;

    @Size(min = 8, max = 64)
    private String description;
}
package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Schema(name = "CreateOrUpdateAd")
@Data
public class CreateOrUpdateAdDTO {
    @Size(min = 4, max = 32, message = "Длина названия должна быть от 4 до 32 символов")
    private String title;

    @Min(0)
    @Max(10_000_000)
    private Integer price;

    @Size(min = 8, max = 64, message = "Длина описания должна быть от 8 до 64 символов")
    private String description;
}

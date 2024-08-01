package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class CreateOrUpdateAdDto {
    @JsonProperty("description") // описание объявления
    private String description;

    @JsonProperty("title") // заголовок объявления
    private String title;

    @JsonProperty("price") // цена объявления
    @Min(0)
    @Max(10000000)
    private Integer price;
}

package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAd {
    /*
    заголовок объявления
     */
    @NotBlank
    @Size(min = 4, max = 32)
    private String title;
    /*
    цена объявления
     */
    @Min(0)
    @Max(10000000)
    private Integer price;
    /*
    описание объявления
     */
    @NotBlank
    @Size(min = 8, max = 64)
    private String description;
}

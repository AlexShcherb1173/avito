package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CreateOrUpdateAd {
    //                              Объявление создаваемое/корректируемое (DTO)

    @Schema(description = "Заголовок объявления")
    @Max(32)
    @Min(4)
    private String title;       //  Заголовок объявления
    //                              minLength 4
    //                              maxLength 32

    @Schema(description = "Цена объявления")
    @Max(10000000)
    @Min(0)
    private int price;          //  Цена объявления
    //                              min 0
    //                              max 10000000

    @Schema(description = "Описание объявления")
    @Max(64)
    @Min(8)
    private String description; //  Описание объявления
    //                              minLength 8
    //                              maxLength 64

}

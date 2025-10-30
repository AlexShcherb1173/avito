package ru.skypro.homework.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для создания или обновления рекламного объявления")
public class CreateOrUpdateAd {
    @Schema(
            type = "string",
            description = "заголовок объявления"
    )
    @Size(min = 4, max = 32)
    private String title;

    @Schema(
            type = "integer",
            format = "int32",
            description = "цена объявления"
    )
    @Min(value = 0)
    @Max(value = 10000000)
    private Integer price;

    @Schema(
            type = "string",
            description = "описание объявления"
    )
    @Size(min = 8, max = 64)
    private String description;
}

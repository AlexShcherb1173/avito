package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;

@Data
public class CreateOrUpdateAdDTO {

    @Schema(description = "Заголовок объявления", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Заголовок должен содержать от 4 до 32 символов")
    @NotBlank(message = "Заголовок не должен быть пустым")
    private String title;

    @Schema(description = "Цена объявления", minimum = "0", maximum = "10000000")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Max(value = 10000000, message = "Цена не может превышать 10 000 000")
    @NotNull(message = "Цена не должна быть пустой")
    private int price;

    @Schema(description = "Описание объявления", minLength = 8, maxLength = 64)
    @Size(min = 8, max = 64, message = "Описание должно содержать от 8 до 64 символов")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
}

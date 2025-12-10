package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO для создания или обновления комментария.
 * Содержит текстовое содержимое комментария.
 */
@Data
@Schema(description = "Создание или обновление комментария")
public class CreateOrUpdateComment {

    @NotBlank
    @Size(min = 8, max = 64)
    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    private String text;
}
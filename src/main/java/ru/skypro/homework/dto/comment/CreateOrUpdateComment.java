package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Данные для создания/обновления комментария")
public class CreateOrUpdateComment {
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
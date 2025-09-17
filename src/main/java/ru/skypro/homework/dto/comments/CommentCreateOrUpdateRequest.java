package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание или обновление комментария")
public class CommentCreateOrUpdateRequest {
    @Size(min = 8, max = 64, message = "Текст комментария")
    @Schema(description = "Текст комментария")
    private String text;
}
package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO для создания и обновления комментариев
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateComment {
    /**
     * текст комментария
     */
    @Schema(description = "текст комментария")
    @NotBlank(message = "Комментарий не может быть пуст")
    @Size(min = 8, message = "Комментарий должен содержать минимум 8 символов")
    @Size(max = 64, message = "Комментарий должен содержать максимум 64 символа")
    private String text;
}

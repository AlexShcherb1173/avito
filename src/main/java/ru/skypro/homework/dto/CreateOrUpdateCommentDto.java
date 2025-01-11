package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий DTO для создания или обновления комментария.
 * Используется для передачи данных о комментарии между слоями приложения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCommentDto {

    /**
     * Текст комментария.
     * Должен содержать от 8 до 64 символов.
     * Поле обязательно для заполнения.
     */
    @Schema(required = true, type = "string", description = "текст комментария", minLength = 8, maxLength = 64)
    @Size(min = 8, max = 64, message = "Текст комментария должен содержать от 8 до 64 символов")
    @NotBlank(message = "Текст комментария обязателен")
    private String text;
}
package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс, представляющий DTO для списка комментариев.
 * Используется для передачи списка комментариев и общего количества комментариев.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {

    /**
     * Общее количество комментариев.
     * Поле не может быть отрицательным.
     */
    @Schema(type = "integer", format = "int32", description = "общее количество комментариев")
    @Min(value = 0, message = "Количество комментариев не может быть отрицательным")
    private Integer count;

    /**
     * Список комментариев.
     * Поле обязательно для заполнения.
     */
    @Schema(description = "Список комментариев")
    @NotNull(message = "Список комментариев обязателен")
    private List<CommentDto> results;
}
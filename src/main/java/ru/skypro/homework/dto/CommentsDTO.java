package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для списка комментариев.
 * Используется для передачи данных о группе комментариев, включая общее количество и сами комментарии.
 * Включает поле для общего количества комментариев, списка комментариев и отдельного комментария.
 */
@Data
public class CommentsDTO {

    @Schema(description = "Общее количество комментариев")
    private int count;

    private CommentDTO comment;

    @Schema(description = "Список комментариев")
    private List<CommentDTO> results;

    public CommentsDTO(int count, List<CommentDTO> results) {
        this.count = count;
        this.results = results;
    }
}

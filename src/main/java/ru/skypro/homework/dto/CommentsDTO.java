package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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

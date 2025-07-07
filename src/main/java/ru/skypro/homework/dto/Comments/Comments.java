package ru.skypro.homework.dto.Comments;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class Comments {
    @Schema(description = "общее количество комментариев")
    private Integer count;

    @Schema(description = "список комментариев")
    private List<CommentDTO> results;
}

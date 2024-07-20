package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CommentsDto {

    @Schema(description = "общее количество комментариев")
    private int type;

    @Schema
    private List<CommentDto> results;
}

package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CommentsResponse {
    /**
     * общее количество комментариев
     */
    @Schema(description = "общее количество комментариев")
    private Integer count;
    /**
     * {@link CommentResponse}
     */
    private List<CommentResponse> results;
}

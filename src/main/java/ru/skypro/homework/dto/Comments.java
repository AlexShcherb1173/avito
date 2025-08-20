package ru.skypro.homework.dto;

import ru.skypro.homework.dto.Comment;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for comments with pagination")
@Data
@AllArgsConstructor
public class Comments {
    @Schema(description = "Total number of comments", example = "10")
    private int count;

    @Schema(description = "List of comments", example = "[{\"id\": 1, \"text\": \"Great post!\"}, {\"id\": 2, \"text\": \"Thanks for sharing!\"}]")
    private List<Comment> results;
}

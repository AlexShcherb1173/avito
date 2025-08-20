package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Comment entity representing a comment in the system")
@Data
@AllArgsConstructor
public class Comment {
    @Schema(description = "Unique identifier author", example = "1")
    private int authorId;
    @Schema(description = "Author's image")
    private String authorImage;
    @Schema(description = "Author's firstname", example = "John")
    private String authorFirstName;
    @Schema(description = "Time of create comment")
    private int createdAt;
    @Schema(description = "Unique identifier of the comment", example = "123")
    private int pk;
    @Schema(description = "Text content of the comment", example = "This is a sample comment.")
    private String text;
}

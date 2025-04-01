package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@AllArgsConstructor
@Schema(name = "Comment")
@Data
public class CommentDTO {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public CommentDTO() {
        this.createdAt = Instant.now().toEpochMilli();
    }
}

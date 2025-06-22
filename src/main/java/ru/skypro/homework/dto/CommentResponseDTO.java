package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentResponseDTO {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt = System.currentTimeMillis();
    private Integer pk;
    private String text;
}

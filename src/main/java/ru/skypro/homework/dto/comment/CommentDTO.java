package ru.skypro.homework.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommentDTO {
    private Integer pk;
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private long createdAt;
    private String text;
    }
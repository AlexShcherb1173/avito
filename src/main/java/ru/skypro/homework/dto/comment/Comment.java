package ru.skypro.homework.dto.comment;

import lombok.Data;

/**
 * DTO для предоставления комментария
 */
@Data
public class Comment {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;
}

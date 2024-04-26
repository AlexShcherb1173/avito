package ru.skypro.homework.dto.rq.comment;

import lombok.Data;

@Data
public class Comment {
    /**
     * Поля относящиеся к сущности UserEntity
     */
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    /**
     * Поля относящиеся к сущности CommentEntity
     */
    private Long createdAt;
    private Integer id;
    private String text;
}

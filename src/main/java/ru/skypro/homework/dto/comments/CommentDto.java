package ru.skypro.homework.dto.comments;

import lombok.Data;

@Data
public class CommentDto {
    private Integer author;     //id автора комментария
    private String authorImage;     //ссылка на аватар автора комментария
    private String authorFirstName;
    private Long createAt;
    private Integer pk;
    private String text;
}

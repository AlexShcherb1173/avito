package ru.skypro.homework.dto.comments;

import lombok.Data;

@Data
public class CommentDto {
    private int author;     //id автора комментария
    private String authorImage;     //ссылка на аватар автора комментария
    private String authorFirstName;
    private long createAt;
    private int pk;
    private String text;
}

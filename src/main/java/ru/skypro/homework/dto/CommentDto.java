package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String text;
    private Integer pk;
    private Integer author;
    private String authorFirstName;
    private String authorImage;
    private Long createdAt;
}
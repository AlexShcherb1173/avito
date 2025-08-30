package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private String text;
    private String author;
    private String createdAt;
}
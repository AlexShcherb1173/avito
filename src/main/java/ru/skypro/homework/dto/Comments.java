package ru.skypro.homework.dto;

import lombok.Data;

import javax.xml.stream.events.Comment;
import java.util.List;

@Data
public class Comments {

    private Integer count;          // Общее количество комментариев
    private List<Comment> results;  // Список комментариев

    public Comments() {

    }
}

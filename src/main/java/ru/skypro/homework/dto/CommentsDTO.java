package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentsDTO {

    /*общее количество комментариев*/
    private int count;
    private List<CommentDTO> results;
}

package ru.skypro.homework.dto.rs.comment;

import lombok.Data;
import ru.skypro.homework.dto.rq.comment.Comment;

import java.util.List;

@Data
public class Comments {
    private Integer count;
    private List<Comment> results;
}

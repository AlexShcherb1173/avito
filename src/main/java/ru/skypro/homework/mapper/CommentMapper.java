package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.entity.CommentEntity;


import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public Comment toDto(CommentEntity entity) {
        Comment dto = new Comment();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(entity.getAuthor().getImage());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPk(entity.getPk());
        dto.setText(entity.getText());
        return dto;
    }


    public Comments toCommentsDto(List<CommentEntity> entities) {
        Comments comments = new Comments();
        comments.setCount(entities.size());
        comments.setResults(entities.stream()
                .map(entity->this.toDto(entity))
                .collect(Collectors.toList()));
        return comments;
    }
}

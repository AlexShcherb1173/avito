package ru.avito.mapper;

import org.springframework.stereotype.Component;
import ru.avito.dto.comment.CommentDto;
import ru.avito.entity.Comment;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getAuthor().getId(),
                comment.getAuthor().getImage(),
                comment.getAuthor().getFirstName(),
                comment.getCreatedAt().toEpochMilli(),
                comment.getId(),
                comment.getText()
        );
    }
}
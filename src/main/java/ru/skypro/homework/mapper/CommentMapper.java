package ru.skypro.homework.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
@Component
@Slf4j
public class CommentMapper {
    private final CommentRepository commentRepository;


    public CommentMapper(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getUser().getId());
        return commentDto;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setAuthor(comment.getUser().getId());
        commentDto.setAuthorFirstName(comment.getUser().getFirstName());
        if (comment.getUser().getImage() != null) {
            commentDto.setAuthorImage("/image/" + comment.getUser().getImage().getId());
        }
        // Установите authorImage, если есть соответствующее поле в User
        // commentDto.setAuthorImage(comment.getUser().getImage());
        return commentDto;
    }
}

//@Mapper

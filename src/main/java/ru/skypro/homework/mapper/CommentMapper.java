package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

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
}

//@Mapper

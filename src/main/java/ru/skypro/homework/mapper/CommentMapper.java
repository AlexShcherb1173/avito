package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.CommentEntity;

public class CommentMapper {

    //из дто в сущность
    public CommentEntity toCommentEntity(Comment comment) {
        if (comment == null) {
            throw new NullPointerException("Переданный объект comment is null");
        }
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(comment.getAuthor());
        commentEntity.setAuthorImage(comment.getAuthorImage());
        commentEntity.setAuthorFirstName(comment.getAuthorFirstName());
        commentEntity.setCreateAd(comment.getCreateAd());
        commentEntity.setText(comment.getText());
        return commentEntity;
    }

    //из сущности в дто Comment
    public Comment toCommentDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            throw new NotFoundException("Переданный объект commentEntity is null");
        }
        Comment comment = new Comment();
        comment.setAuthor(commentEntity.getAuthor());
        comment.setAuthorImage(commentEntity.getAuthorImage());
        comment.setAuthorFirstName(commentEntity.getAuthorFirstName());
        comment.setCreateAd(commentEntity.getCreateAd());
        comment.setText(commentEntity.getText());
        return comment;
    }
}

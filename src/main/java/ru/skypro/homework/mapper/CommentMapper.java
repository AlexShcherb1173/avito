package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    //Преобразование сущности в DTO комментария

    public Comment toCommentDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setPk(commentEntity.getId());
        comment.setAuthor(commentEntity.getAuthor().getId());
        comment.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        comment.setAuthorImage(commentEntity.getAuthor().getImagePath());
        comment.setCreatedAt(commentEntity.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC) * 1000);
        comment.setText(commentEntity.getText());
        return comment;
    }

    //Преобразование DTO создания в сущность

    public CommentEntity toCommentEntity(CreateOrUpdateComment createComment, UserEntity author, AdEntity ad) {
        if (createComment == null) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(createComment.getText());
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(LocalDateTime.now());
        return commentEntity;
    }

    //Обновление существующей сущности данными из DTO

    public void updateCommentEntityFromDto(CommentEntity commentEntity, CreateOrUpdateComment updateComment) {
        if (commentEntity == null || updateComment == null) {
            return;
        }

        commentEntity.setText(updateComment.getText());
    }
} 
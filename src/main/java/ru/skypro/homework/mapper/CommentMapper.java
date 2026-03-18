package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class CommentMapper {

    public Comment toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setPk(entity.getId());
        comment.setAuthor(entity.getAuthor().getId());
        comment.setAuthorFirstName(entity.getAuthor().getFirstName());
        comment.setAuthorImage(entity.getAuthor().getImage());
        comment.setCreatedAt(entity.getCreatedAt());
        comment.setText(entity.getText());
        return comment;
    }

    public CommentEntity fromDto(CreateOrUpdateComment dto, UserEntity author, AdEntity ad) {
        if (dto == null || author == null || ad == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setText(dto.getText());
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setAuthor(author);
        entity.setAd(ad);
        return entity;
    }

    public void updateCommentFields(CreateOrUpdateComment dto, CommentEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setText(dto.getText());
    }
}
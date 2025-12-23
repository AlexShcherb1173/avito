package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.Instant;

@Component
public class CommentMapperManual {

    public Comment toDto(CommentEntity entity) {
        if (entity == null) return null;

        Comment dto = new Comment();
        dto.setPk(entity.getId());
        dto.setText(entity.getText());
        dto.setCreatedAt(entity.getCreatedAt() == null ? null : entity.getCreatedAt().toEpochMilli());

        UserEntity author = entity.getAuthor();
        if (author != null) {
            dto.setAuthor(author.getId());
            dto.setAuthorFirstName(author.getFirstName());
            dto.setAuthorImage(author.getImage());
        }

        return dto;
    }

    public void applyCreateOrUpdate(CommentEntity entity, CreateOrUpdateComment source) {
        if (entity == null || source == null) return;
        entity.setText(source.getText());
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
    }
}


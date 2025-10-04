package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.CommentEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class CommentMapper {

    public CommentDto toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setPk(entity.getId());
        dto.setText(entity.getText());

        //конвертация LocalDateTime в миллисекунды
        if (entity.getCreatedAt() != null) {
            Instant instant = entity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant();
            dto.setCreateAt(instant.toEpochMilli());
        }

        if (entity.getAuthor() != null) {
            dto.setAuthor(entity.getAuthor().getId());
            dto.setAuthorImage(entity.getAuthor().getImage());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        }

        return dto;
    }

    public CommentEntity toEntity(CreateOrUpdateCommentDto dto) {
        if (dto == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setText(dto.getText());
        entity.setCreatedAt(LocalDateTime.now()); //устанавливаем текущее время

        return entity;
    }

    public void updateEntityFromDto(CommentEntity entity,
                                    CreateOrUpdateCommentDto dto) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getText() != null) {
            entity.setText(dto.getText());
        }
    }
}

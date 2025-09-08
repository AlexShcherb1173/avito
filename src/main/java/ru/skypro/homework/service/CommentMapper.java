package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.ZoneOffset;

@Service
public class CommentMapper {

    public Comment toDto(CommentEntity e) {
        if (e == null) return null;
        Comment dto = new Comment();
        dto.setPk(e.getId().intValue());
        dto.setAuthor(e.getAuthor().getId().intValue());
        dto.setCreatedAt(e.getCreatedAt().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        dto.setText(e.getText());
        dto.setAuthorImage(e.getAuthor().getImage());
        dto.setAuthorFirstName(e.getAuthor().getFirstName());
        return dto;
    }

    public CommentEntity fromCreateRequest(CreateOrUpdateComment req, UserEntity author, AdEntity ad) {
        if (req == null || author == null || ad == null) return null;
        return CommentEntity.builder()
                .text(req.getText())
                .author(author)
                .ad(ad)
                .build();
    }
}

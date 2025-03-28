package ru.skypro.homework.mappers;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.CommentEntity;
import ru.skypro.homework.models.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(CommentEntity comment) {
        CommentDto dto = new CommentDto();
        dto.setAuthor(comment.getAuthor().getId());
        dto.setPk(comment.getAd().getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAd() != null ?
                comment.getCreatedAd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : 0);
        dto.setText(comment.getText());
        dto.setAuthorFirstName(comment.getAuthor().getFirstName());
        dto.setAuthorImage(comment.getAuthor().getImage());
        return dto;
    }


    public CommentsDto adsDto(List<CommentEntity> comments) {
        CommentsDto dto = new CommentsDto();
        dto.setCount(comments.size());
        List<CommentDto> list = new ArrayList<>();
        for (CommentEntity comment : comments) {
            CommentDto commentDto = toCommentDto(comment);
            list.add(commentDto);
        }
        dto.setResults(list);
        return dto;
    }

    public CommentEntity toCommentEntity(CreateOrUpdateCommentDto dto, AdEntity ad, UserEntity user) {
        CommentEntity comment = new CommentEntity();
        comment.setText(dto.getText());
        comment.setCreatedAd(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setAd(ad);
        return comment;
        }
}

package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.User;

public class CommentMapper {

    public static CommentDto toDto(ru.skypro.homework.model.Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setAuthor(entity.getAuthor().getId());
        return dto;
    }

    public static ru.skypro.homework.model.Comment toEntity(CreateOrUpdateCommentDto dto, User author, ru.skypro.homework.model.Ad ad) {
        ru.skypro.homework.model.Comment entity = new ru.skypro.homework.model.Comment();
        entity.setText(dto.getText());
        entity.setAuthor(author);
        entity.setAd(ad);
        return entity;
    }
}
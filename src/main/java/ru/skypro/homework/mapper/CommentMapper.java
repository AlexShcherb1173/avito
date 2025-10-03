package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.User;

/**
 * Маппер для преобразования между сущностью Comment и DTO объектами.
 * Обеспечивает конвертацию данных между слоем базы данных и API.
 */
public class CommentMapper {

    /**
     * Преобразует сущность Comment в DTO объект.
     *
     * @param entity сущность комментария из базы данных
     * @return DTO объект для передачи клиенту
     */
    public static CommentDto toDto(ru.skypro.homework.model.Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setAuthor(entity.getAuthor().getId());
        return dto;
    }

    /**
     * Преобразует DTO объект в сущность Comment.
     *
     * @param dto DTO объект с данными от клиента
     * @param author сущность пользователя-автора комментария
     * @param ad сущность объявления, к которому относится комментарий
     * @return сущность комментария для сохранения в базу данных
     */
    public static ru.skypro.homework.model.Comment toEntity(CreateOrUpdateCommentDto dto, User author, ru.skypro.homework.model.Ad ad) {
        ru.skypro.homework.model.Comment entity = new ru.skypro.homework.model.Comment();
        entity.setText(dto.getText());
        entity.setAuthor(author);
        entity.setAd(ad);
        return entity;
    }
}
package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
/**
 * Класс для преобразования (маппинга) между сущностями и DTO - комментарии.
 */
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getUser().getId());
        commentDto.setCreatedAt(comment.getCreatedAt());
        return commentDto;
    }

    public static Comment toComment(CreateOrUpdateCommentDto createOrUpdateCommentDto, User user, Long time, Ad ad) {
        Comment comment = new Comment();
        comment.setId(null);
        comment.setText(createOrUpdateCommentDto.getText());
        comment.setAd(ad);
        comment.setCreatedAt(time);
        comment.setUser(user);
        return comment;
    }
}

package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    /**
     * Получение комментариев объявления
     * @param adId = id объявления
     * @return = лист с комментариями
     */
    CommentsDto getComments(int adId);

    /**
     * Добавление комментария к объявлению
     * @param adId = id объявления
     * @param comment = текст комментария
     * @return = CommentDto
     */
    CommentDto postComment(int adId, CreateOrUpdateCommentDto comment, String userName);

    /**
     * Удалить комментарий
     * @param id = id комментария
     */
    void deleteComment(int id);

    /**
     * Обновление комментария
     * @param commentId = id комментария
     * @param commentDto = текст комментария
     * @return = CommentDto
     */
    CommentDto patchComment(int adId, int commentId, CreateOrUpdateCommentDto commentDto, String userName);
}

package ru.skypro.homework.service;

import jakarta.transaction.Transactional;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentServiceApi {

    /**
     * Добавление комментария к объявлению
     *
     * @param id         объявления
     * @param newComment {@link Comment} для сохранения
     * @return {@link Comment} сохраненный комментарий
     */
    @Transactional
    Comment addCommentToAd(Long id, Comment newComment);

    /**
     * Удаление комментария к объявлению
     *
     * @param adId      объявления
     * @param commentId комментария
     */
    @Transactional
    void deleteCommentFromAd(Long adId, Long commentId);

    /**
     * Обновление комментария объявления
     *
     * @param adId                  идентификатор объявления
     * @param commentId             идентификатор комментария
     * @param createOrUpdateComment {@link CreateOrUpdateComment}
     * @return {@link Comment} измененный комментарий
     */
    Comment updateComment(
            Long adId,
            Long commentId,
            CreateOrUpdateComment createOrUpdateComment
    );

    /**
     * Получение комментариев объявления
     *
     * @param id идентификатор объявления
     */
    List<Comment> getCommentsForAd(Long id);

    /**
     * Получение комментария по id
     *
     * @param id идентификатор комментария
     * @return {@link Comment}
     */
    Comment getCommentById(Long id);

}

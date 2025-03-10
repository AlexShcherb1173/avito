package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

/**
 * Сервисный интерфейс для работы с комментариями.
 *
 * Интерфейс предоставляет методы для выполнения операций с комментариями:
 * - Преобразование сущности {@link Comment} в {@link CommentDTO} {@link #getCommentDTO(Comment)}.
 * - Обновление существующего комментария {@link #updateComment(Integer, CreateOrUpdateCommentDTO)}.
 * - Добавление нового комментария к объявлению {@link #addComment(Integer, CreateOrUpdateCommentDTO)}.
 * - Получение списка комментариев для объявления {@link #getComments(Integer)}.
 * - Создание сущности {@link Comment} на основе данных {@link CommentDTO} {@link #createCommentFromDTO(CommentDTO)}.
 * - Проверка, является ли пользователь автором комментария {@link #isCommentAuthor(Integer, String)}.
 * - Удаление комментария {@link #deleteComment(Integer)}.
 *
 * Методы интерфейса возвращают объекты DTO, такие как {@link CommentDTO} и {@link CommentsDTO}, а также выполняют операции обновления и получения данных.
 */
public interface CommentService {

    CommentDTO getCommentDTO(Comment comment);

    CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO commentDTO);

    CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO commentDTO);

    CommentsDTO getComments(Integer adId);

    Comment createCommentFromDTO(CommentDTO commentDTO);

    boolean isCommentAuthor(Integer commentId, String username);

    void deleteComment(Integer commentId);
}

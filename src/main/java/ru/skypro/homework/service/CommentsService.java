package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;

public interface CommentsService {
    // Получение комментариев объявления
    Comments getComments(long id) throws ObjectNotFoundException;

    // Добавление комментария к объявлению
    Comment addComment(long id, CreateOrUpdateComment createdComment) throws ObjectNotFoundException;

    // Удаление комментария
    void deleteComment(long adId, long commentId) throws ObjectNotFoundException, ForbiddenException;

    // Обновление комментария
    Comment updateComment(long adId, long commentId, CreateOrUpdateComment updatedComment) throws ObjectNotFoundException, ForbiddenException;

}

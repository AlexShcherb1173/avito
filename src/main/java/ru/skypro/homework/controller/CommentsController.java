package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

import java.util.List;

/**
 * REST-контроллер для обработки HTTP-запросов, связанных с комментариями к объявлениям.
 * Предоставляет API для операций CRUD над комментариями.
 */
@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    /**
     * Получает список всех комментариев для указанного объявления.
     *
     * @param adId идентификатор объявления
     * @return список DTO комментариев
     */
    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer adId) {
        return commentService.getCommentsByAdId(adId);
    }

    /**
     * Добавляет новый комментарий к объявлению.
     * Требует авторизации пользователя.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param adId идентификатор объявления, к которому добавляется комментарий
     * @param comment DTO с данными комментария
     * @return DTO созданного комментария
     */
    @PostMapping
    public CommentDto addComment(Authentication authentication,
                                 @PathVariable Integer adId,
                                 @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.addComment(authentication, adId, comment);
    }

    /**
     * Удаляет комментарий по идентификатору.
     * Удалять может только автор комментария или администратор.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param adId идентификатор объявления (используется для валидации)
     * @param commentId идентификатор комментария для удаления
     */
    @DeleteMapping("/{commentId}")
    public void deleteComment(Authentication authentication,
                              @PathVariable Integer adId,
                              @PathVariable Integer commentId) {
        commentService.deleteComment(authentication, commentId);
    }

    /**
     * Обновляет существующий комментарий.
     * Редактировать может только автор комментария или администратор.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param adId идентификатор объявления (используется для валидации)
     * @param commentId идентификатор комментария для обновления
     * @param comment DTO с обновленными данными комментария
     * @return DTO обновленного комментария
     */
    @PatchMapping("/{commentId}")
    public CommentDto updateComment(Authentication authentication,
                                    @PathVariable Integer adId,
                                    @PathVariable Integer commentId,
                                    @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.updateComment(authentication, commentId, comment);
    }
}
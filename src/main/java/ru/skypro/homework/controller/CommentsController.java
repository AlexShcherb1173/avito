package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;

import javax.validation.Valid;

/**
 * REST контроллер для управления комментариями к объявлениям.
 * Обрабатывает HTTP запросы связанные с созданием, получением, обновлением и удалением комментариев.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class CommentsController {

    private final CommentsService commentsService;

    /**
     * Получает все комментарии к указанному объявлению.
     *
     * @param id идентификатор объявления
     * @return ResponseEntity с объектом {@link Comments} и статусом 200 OK,
     *         или 404 Not Found если объявление не существует
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        log.info("Getting comments for ad with id: {}", id);
        try {
            Comments comments = commentsService.getComments(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Добавляет новый комментарий к объявлению.
     * Требуется аутентификация пользователя.
     *
     * @param id идентификатор объявления
     * @param comment данные комментария
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с созданным комментарием и статусом 200 OK,
     *         или 401 Unauthorized при ошибке аутентификации
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id,
                                              @RequestBody @Valid CreateOrUpdateComment comment,
                                              Authentication authentication) {
        log.info("Adding comment to ad with id: {} by user: {}", id, authentication.getName());
        try {
            Comment createdComment = commentsService.addComment(id, comment, authentication.getName());
            return ResponseEntity.ok(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Удаляет комментарий.
     * Доступно только администраторам или авторам комментария.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity со статусом 200 OK при успешном удалении,
     *         403 Forbidden при недостаточных правах,
     *         401 Unauthorized при ошибке аутентификации
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId,
                                           Authentication authentication) {
        log.info("Deleting comment with id: {} from ad with id: {} by user: {}",
                commentId, adId, authentication.getName());
        try {
            commentsService.deleteComment(adId, commentId, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновляет комментарий.
     * Доступно только администраторам или авторам комментария.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment обновленные данные комментария
     * @param authentication объект аутентификации Spring Security
     * @return ResponseEntity с обновленным комментарием и статусом 200 OK,
     *         403 Forbidden при недостаточных правах,
     *         401 Unauthorized при ошибке аутентификации
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody @Valid CreateOrUpdateComment comment,
                                                 Authentication authentication) {
        log.info("Updating comment with id: {} for ad with id: {} by user: {}",
                commentId, adId, authentication.getName());
        try {
            Comment updatedComment = commentsService.updateComment(adId, commentId, comment, authentication.getName());
            return ResponseEntity.ok(updatedComment);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
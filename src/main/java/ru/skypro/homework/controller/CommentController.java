package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.service.CommentService;

/**
 * Контроллер для управления комментариями к объявлениям.
 * Доступ к методам контролируется с помощью аннотации {@link PreAuthorize},
 * что позволяет ограничить выполнение действий только для авторизованных пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
public class CommentController {

    private final CommentService commentService;

    /**
     * Получает список всех комментариев к объявлению.
     *
     * @param id ID объявления
     * @return {@link ResponseEntity} с объектом {@link CommentsDTO}, содержащим список комментариев
     */
    @GetMapping("/{id}/comments")
    @Operation(summary = "Получить комментарии")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable Integer id) {
        CommentsDTO comments = commentService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param id      ID объявления
     * @param comment объект {@link CreateOrUpdateCommentDTO}, содержащий текст комментария
     * @return {@link ResponseEntity} с объектом {@link CommentDTO}, содержащим данные созданного комментария
     */
    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавить комментарий")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Integer id, @RequestBody CreateOrUpdateCommentDTO comment) {
        CommentDTO newComment = commentService.addComment(id, comment);
        return ResponseEntity.ok(newComment);
    }

    /**
     * Удаляет комментарий к объявлению.
     * Доступно только автору комментария или администратору.
     *
     * @param adId      ID объявления (не используется в методе, но нужен в URL)
     * @param commentId ID комментария, который нужно удалить
     * @return {@link ResponseEntity} со статусом 200 OK при успешном удалении
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удалить комментарий")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#commentId, authentication.name)")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет текст комментария.
     * Доступно только автору комментария или администратору.
     *
     * @param adId      ID объявления (не используется в методе, но нужен в URL)
     * @param commentId ID комментария, который нужно обновить
     * @param comment   объект {@link CreateOrUpdateCommentDTO}, содержащий обновленный текст
     * @return {@link ResponseEntity} с обновленным {@link CommentDTO}
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновить комментарий")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#commentId, authentication.name)")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateCommentDTO comment) {
        CommentDTO updatedComment = commentService.updateComment(commentId, comment);
        return ResponseEntity.ok(updatedComment);
    }
}
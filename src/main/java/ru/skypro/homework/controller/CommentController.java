package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;
/**
 * Контроллер для работы с комментариями.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    /**
     * Конструктор контроллера CommentController.
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    /**
     * Получение комментариев объявления.
     */
    @GetMapping("/{adId}/comments")
    @Operation(summary = "Получение комментариев объявления")
    public CommentsDto getComments(@PathVariable Integer adId) {
        CommentsDto commentsDto = commentService.getCommentsForAd(adId);
        if (commentsDto.getCount() < 1) {
            log.info("Комментариев нет");
        }
        return commentsDto;
    }
    /**
     * Добавление комментария к объявлению.
     */
    @PostMapping("/{adId}/comments")
    @Operation(summary = "Добавление комментария к объявлению")
    public CommentDto addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.addComment(adId, comment);
    }
    /**
     * Обновление комментария.
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновление комментария")
    public CommentDto updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.updateComment(adId, commentId, comment);
    }
    /**
     * Удаление комментария.
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }
}
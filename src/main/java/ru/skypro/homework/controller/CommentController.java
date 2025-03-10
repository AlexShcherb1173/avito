package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.exceptions.AccessDeniedException;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/ads/{id}/comments")
@Tag(name = "Комментарии")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping
    public CommentsDTO getComments(@PathVariable("id") int adId) {
        return commentService.getComments(adId);
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping
    public Long addComment(@PathVariable("id") int adId,
                          @RequestBody CreateOrUpdateComment createComment) {
        return commentService.addComment(adId, createComment);
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("id") int adId,
                              @PathVariable("commentId") Long commentId) throws AccessDeniedException {
        commentService.deleteComment(adId, commentId);
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable("id") int adId,
                                 @PathVariable("commentId") Long commentId,
                                 @RequestBody CreateOrUpdateComment updateComment) throws AccessDeniedException {
        return commentService.updateComment(adId, commentId, updateComment);
    }
}

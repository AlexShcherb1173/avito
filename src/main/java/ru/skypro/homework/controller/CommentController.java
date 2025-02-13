package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.exceptions.AccessDeniedException;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;

import java.security.Principal;


@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Комментарии")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable("id") int adId) {
        return commentService.getComments(adId);
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable("id") Long adId,
                           @RequestBody CreateOrUpdateComment createComment,
                              @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.addComment(adId, createComment, userDetails.getUsername());
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable("id") int adId,
                              @PathVariable("commentId") Long commentId) throws AccessDeniedException {
        commentService.deleteComment(adId, commentId);
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{id}/comments/{commentId}")
    public Comment updateComment(@PathVariable("id") int adId,
                                 @PathVariable("commentId") Long commentId,
                                 @RequestBody CreateOrUpdateComment updateComment) throws AccessDeniedException {
        return commentService.updateComment(adId, commentId, updateComment);
    }
}

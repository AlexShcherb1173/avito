package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Получение комментариев объявления
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Long adId) {
        Comments comments = commentService.getCommentsByAdId(adId);
        return ResponseEntity.ok(comments);
    }

    // Добавление комментария к объявлению
    @PostMapping("/{id}/comments")
    public ResponseEntity<Long> addComment(@PathVariable Integer adId,
                                           @RequestBody Comment comment) {
        Long createdComment = commentService.addComment(adId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long adId,
                                              @PathVariable Long commentId) {
        commentService.deleteComment(Math.toIntExact(adId), Math.toIntExact(commentId));
        return ResponseEntity.noContent().build();
    }

    // Обновление комментария
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long adId,
                                                 @PathVariable Long commentId,
                                                 @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(adId, commentId, comment);
        return ResponseEntity.ok(updatedComment);
    }
}
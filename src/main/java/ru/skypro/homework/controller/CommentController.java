package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable int id,
            @RequestBody CreateOrUpdateComment createComment
    ) {
        return ResponseEntity.ok(commentService.addComment(id, createComment));
    }

    @Operation(summary = "Удаление комментария")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable int adId,
            @PathVariable int commentId
    ) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable int adId,
            @PathVariable int commentId,
            @RequestBody CreateOrUpdateComment updateComment
    ) {
        return ResponseEntity.ok(
                commentService.updateComment(adId, commentId, updateComment)
        );
    }
}
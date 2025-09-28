package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    @Operation(summary = "получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer adId) {
        Comments comments = new Comments();
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public ResponseEntity<CreateOrUpdateComment> addComment(@PathVariable("id") Integer adId) {
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        return ResponseEntity.ok(createOrUpdateComment);
    }

    @Operation(summary = "удаление комментария")
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "обновление комментария")
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId) {
        Comment comment = new Comment();
        return ResponseEntity.ok().body(comment);
    }
}

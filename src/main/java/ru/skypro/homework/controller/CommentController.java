package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;


@RestController
@RequestMapping("/ads/{adId}/comments")
@Tag(name = "Комментарии", description = "CRUD операции для комментариев")
public class CommentController {

    @GetMapping
    @Operation(summary = "Получение комментариев объявления")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(new Comments());
    }

    @PostMapping
    @Operation(summary = "Добавление комментария к объявлению")
    public ResponseEntity<Comment> addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удаление комментария")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Обновление комментария")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }
}
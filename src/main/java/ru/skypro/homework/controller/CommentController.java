package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    @Operation(summary = "получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable("id") Integer adId) {
        CommentsDto commentsDto = new CommentsDto();
        return ResponseEntity.ok(commentsDto);
    }

    @Operation(summary = "добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public ResponseEntity<CreateOrUpdateCommentDto> addComment(@PathVariable("id") Integer adId) {
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        return ResponseEntity.ok(createOrUpdateCommentDto);
    }

    @Operation(summary = "удаление комментария")
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "обновление комментария")
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto comment) {
        CommentDto commentDto = new CommentDto();
        return ResponseEntity.ok().body(commentDto);
    }
}

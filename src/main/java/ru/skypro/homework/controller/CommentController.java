package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{adId}/comments")
    @Operation(summary = "Получить комментарии объявления")
    public ResponseEntity<CommentsDto> getComments(@PathVariable Integer adPk, Authentication authentication) {
        return ResponseEntity.ok(commentService.getCommentsForAd(adPk, authentication));
    }

    @PostMapping("/{adId}/comments")
    @Operation(summary = "Добавить комментарий")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer adPk,
                                                 @RequestBody CreateOrUpdateCommentDto commentDto,
                                                 Authentication authentication) {
        return ResponseEntity.ok(commentService.addComment(adPk, commentDto, authentication));
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновить комментарий")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adPk,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto commentDto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(commentService.updateComment(adPk, commentId, commentDto, authentication));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удалить комментарий")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adPk,
                                              @PathVariable Integer commentId,
                                              Authentication authentication) {
        commentService.deleteComment(adPk, commentId, authentication);
        return ResponseEntity.noContent().build();
    }
}
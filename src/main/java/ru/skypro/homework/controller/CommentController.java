package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable("id") Integer adId) {
        CommentsDto commentsDto = commentService.getComments(adId);
        return ResponseEntity.ok(commentsDto);
    }

    @Operation(summary = "добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable("id") Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto,
                                                 Authentication authentication) {
        String username = authentication.getName();
        CommentDto commentDto = commentService.createComment(adId, createOrUpdateCommentDto, username);
        return ResponseEntity.ok(commentDto);
    }

    @Operation(summary = "удаление комментария")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId,
                                              Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(adId, commentId, username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "обновление комментария")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto,
                                                    Authentication authentication) {
        String username = authentication.getName();
        CommentDto updatedDto = commentService.updateComment(adId, commentId, createOrUpdateCommentDto, username);
        return ResponseEntity.ok().body(updatedDto);
    }

}

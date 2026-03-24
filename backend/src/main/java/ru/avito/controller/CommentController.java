package ru.avito.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.dto.comment.CommentDto;
import ru.avito.dto.comment.CommentsResponse;
import ru.avito.dto.comment.CreateOrUpdateCommentRequest;
import ru.avito.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<CommentsResponse> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(commentService.getComments(adId));
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Integer adId,
            @Valid @RequestBody CreateOrUpdateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.addComment(adId, request));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @Valid @RequestBody CreateOrUpdateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId
    ) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.noContent().build();
    }
}
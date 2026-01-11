package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id,
                                              @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                              Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(commentService.addComment(id, email, createOrUpdateComment));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId,
                                              Authentication authentication) {
        String email = authentication.getName();
        commentService.deleteComment(adId, commentId, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                 Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, email, createOrUpdateComment));
    }
}

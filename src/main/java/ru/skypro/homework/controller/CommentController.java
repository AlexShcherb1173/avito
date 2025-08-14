package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{adId}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        List<CommentDTO> comments = commentService.getCommentsForAd(adId);
        Comments response = new Comments();
        response.setCount(comments.size());
        response.setResults(comments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer adId,
            @RequestBody CreateOrUpdateComment comment,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(commentService.addComment(adId, comment, username));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            Authentication authentication) {
        commentService.deleteComment(adId, commentId, authentication);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateComment comment,
            Authentication authentication) {
        return ResponseEntity.ok(commentService.editComment(adId, commentId, comment, authentication));
    }
}
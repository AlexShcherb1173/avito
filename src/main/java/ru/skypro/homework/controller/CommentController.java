package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ads/{id}/comments")
@CrossOrigin()
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<CommentsDto> getCommentsAd(@PathVariable int id) {

        return ResponseEntity.ok(commentService.commentsAd(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createCommentForAd(@PathVariable int id,
                                                         @RequestBody CreateOrUpdateCommentDto dto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(commentService.createComment(id, dto, userDetails));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentForAd(@PathVariable int adId,
                                                   @PathVariable int commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateCommentForAd(@PathVariable int adId,
                                                         @PathVariable int commentId,
                                                         @RequestBody CreateOrUpdateCommentDto dto) {

        return ResponseEntity.ok(commentService.updateComment(adId, commentId, dto));
    }
}

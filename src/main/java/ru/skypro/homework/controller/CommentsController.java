package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer adId) {
        return commentService.getCommentsByAdId(adId);
    }

    @PostMapping
    public CommentDto addComment(Authentication authentication,
                                 @PathVariable Integer adId,
                                 @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.addComment(authentication, adId, comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(Authentication authentication,
                              @PathVariable Integer adId,
                              @PathVariable Integer commentId) {
        commentService.deleteComment(authentication, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(Authentication authentication,
                                    @PathVariable Integer adId,
                                    @PathVariable Integer commentId,
                                    @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.updateComment(authentication, commentId, comment);
    }
}
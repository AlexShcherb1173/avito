package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.model.Comment;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@RequestBody CommentDto commentDto, @PathVariable Integer adId, Authentication authentication) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return commentService.addComment(comment, authentication);
    }

    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CommentDto commentDto, Authentication authentication) {
        if (commentService.canEditComment(commentId, authentication)) {
            Comment comment = new Comment();
            comment.setText(commentDto.getText());
            return commentService.addComment(comment, authentication);
        }
        throw new SecurityException("You are not authorized to edit this comment.");
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId, Authentication authentication) {
        if (commentService.canEditComment(commentId, authentication)) {
            commentService.deleteComment(commentId);
        } else {
            throw new SecurityException("You are not authorized to delete this comment.");
        }
    }
}

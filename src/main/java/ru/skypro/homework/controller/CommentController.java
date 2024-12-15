package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    @GetMapping
    public Comments getComments(@PathVariable("adId") Integer adId) {
        Comments comments = null;
        return comments;
    }

    @PostMapping
    public Comment postComment(
            @PathVariable("adId") Integer adId,
            @RequestBody CreateOrUpdateComment createdComment) {
        Comment comment = null;
        return comment;
    }

    @PatchMapping("/{commentId}")
    @Transactional
    @PreAuthorize("@commentService.hasPermission(#adId, #commentId)")
    public Comment patchComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            @RequestBody CreateOrUpdateComment updatedComment) {

        Comment comment = null;
        return comment;
    }

    @DeleteMapping("/{commentId}")
    @Transactional
    @PreAuthorize("@commentService.hasPermission(#adId, #commentId)")
    public void deleteComment(@PathVariable("adId") Integer adId,
                              @PathVariable("commentId") Integer commentId) {
      //  commentService.delete(adId, commentId);
    }

}

package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

import java.security.Principal;

@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable("id") Integer adId) {
        return ResponseEntity.ok(commentService.getComments(adId));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> postComment(@PathVariable("id") Integer adId, @RequestBody CreateOrUpdateCommentDto comment,
                                                  Principal principal) {
        String currentUserName = principal.getName();
        return ResponseEntity.ok(commentService.postComment(adId, comment, currentUserName));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @CheckRoleService.getUserNameByComment(#commentId) == principal.username")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @CheckRoleService.getUserNameByComment(#commentId) == principal.username")
    public ResponseEntity<CommentDto> patchComment(@PathVariable Integer adId,
                                                   @PathVariable Integer commentId,
                                                   @RequestBody CreateOrUpdateCommentDto commentDto,
                                                   Principal principal) {
        return ResponseEntity.ok(commentService.patchComment(adId, commentId, commentDto, principal.getName()));
    }

}

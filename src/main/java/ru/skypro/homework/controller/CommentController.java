package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Slf4j
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    @GetMapping("/{adId}/comments")
    @Operation(summary = "Получить комментарии объявления")
    public ResponseEntity<CommentsDto> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(commentService.getCommentsForAd(adId));
    }

    @PostMapping("/{adId}/comments")
    @Operation(summary = "Добавить комментарий")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto commentDto) {
        log.info("addComment {}", commentDto);
        return ResponseEntity.ok(commentService.addComment(adId, commentDto));
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentServiceImpl.verificationAuthorComment(#commentId)")
    @Operation(summary = "Обновить комментарий")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, commentDto));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentServiceImpl.verificationAuthorComment(#commentId)")
    @Operation(summary = "Удалить комментарий")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.noContent().build();
    }
}
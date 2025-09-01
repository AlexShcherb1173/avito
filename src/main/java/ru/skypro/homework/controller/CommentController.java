package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // Получение всех комментариев к объявлению
    @GetMapping
    public ResponseEntity<CommentsResponse> getComments(@PathVariable Long adId) {
        CommentsResponse response = commentService.getComments(adId);
        return ResponseEntity.ok(response);
    }

    // Добавление нового комментария
    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long adId,
            @RequestBody @Valid CreateOrUpdateComment dto,
            @AuthenticationPrincipal User user) {
        CommentDto comment = commentService.addComment(adId, user.getId(), dto);
        return ResponseEntity.ok(comment);
    }

    //  Удаление комментария
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long adId,
            @PathVariable Long commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    // Редактирование комментария
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long adId,
            @PathVariable Long commentId,
            @RequestBody @Valid CreateOrUpdateComment dto) {
        CommentDto updated = commentService.updateComment(adId, commentId, dto);
        return ResponseEntity.ok(updated);
    }
}

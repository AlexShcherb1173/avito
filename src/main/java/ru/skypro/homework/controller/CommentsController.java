package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("ads")
@Tag(name = "Комментарии")
@RequiredArgsConstructor
public class CommentsController {

    final private CommentService commentService;

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получить комментарии объявления")
    public CommentsDto getComments(@PathVariable("id") Long idAd) {
        return commentService.getComments(idAd);
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавить комментарий к объявлению")
    public CommentDto addComment(@PathVariable("id") Long idAd, @RequestBody CommentCreateOrUpdateRequest request) {
        return commentService.addComment(idAd, request);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удалить комментарий из объявления")
    public ResponseEntity deleteComment(@PathVariable("adId") Long idAd, @PathVariable("commentId") Long idComment) {
        commentService.deleteComment(idAd, idComment);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновить текст комментария")
    public CommentDto updateComment(@PathVariable("adId") Long idAd, @PathVariable("commentId") Long idComment, @RequestBody CommentCreateOrUpdateRequest request) {
        return commentService.updateComment(idAd, idComment, request);
    }
}

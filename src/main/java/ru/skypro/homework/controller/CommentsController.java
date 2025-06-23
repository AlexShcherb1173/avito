package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments.Comment;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Comments.CreateorUpdateComment;

import java.util.Collections;
import java.util.List;

/**
 * Контроллер для работы с комментариями
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Комментарии")
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {


    @Operation(summary = "получение все комментариев")
    @GetMapping
    public List<Comment> getComments(@PathVariable Integer adId) {
        return Collections.emptyList();
    }

    @Operation(summary = "Добавление комментария")
    @PostMapping
    public Comment addComment(@PathVariable Integer adId,
                                              @RequestBody CreateOrUpdateAd comment) {
        return new Comment();
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateorUpdateComment comment) {
        return new Comment();
    }
}

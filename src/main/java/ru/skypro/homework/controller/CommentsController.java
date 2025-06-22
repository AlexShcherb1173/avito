package ru.skypro.homework.controller;

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

    /**
     * Получение комментариев объявления
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    /**
     * Добавление комментария
     */
    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Integer adId,
                                              @RequestBody CreateOrUpdateAd comment) {
        Comment emptyComment = new Comment();
        return ResponseEntity.ok(emptyComment);
    }

    /**
     * Удаление комментария
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление комментария
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateorUpdateComment comment) {
        Comment emptyComment = new Comment();
        return ResponseEntity.ok(emptyComment);
    }
}

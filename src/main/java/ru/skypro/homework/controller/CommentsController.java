package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads") // Базовый путь для комментариев тоже /ads
@Tag(name = "Комментарии")

public class CommentsController {

    @GetMapping("/{id}/comments")
    @Operation (summary = "Получение комментариев объявления")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(new Comments());
    }
    @PostMapping("/{id}/comments")
    @Operation (summary = "Добавление комментария к объявлению")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer id,
                                              @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }
    @DeleteMapping("/{adId}/comments/{commentId}") // Две переменные пути
    @Operation (summary = "Удаление коментария")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation (summary = "Обновление комментария")
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") Integer adId,
                                                 @PathVariable("commentId") Integer commentId,
                                                 @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(new Comment());
    }
}

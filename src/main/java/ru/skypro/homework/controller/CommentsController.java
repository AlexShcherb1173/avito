package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments.CommentDTO;
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
    public List<CommentDTO> getComments(@PathVariable Long adId) {
        return Collections.emptyList();
    }

    @Operation(summary = "Добавление комментария")
    @PostMapping
    public CommentDTO addComment(@PathVariable Long adId,
                                 @RequestBody CreateOrUpdateAd comment) {
        return new CommentDTO();
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long adId,
                                           @PathVariable Long commentId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public CommentDTO updateComment(@PathVariable Long adId,
                                    @PathVariable Long commentId,
                                    @RequestBody CreateorUpdateComment comment) {
        return new CommentDTO();
    }
}

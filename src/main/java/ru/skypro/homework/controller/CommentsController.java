package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads/{adId}/comments")
@Tag(name = "Комментарии")
public class CommentsController {
    private final CommentService service;
    public CommentsController(CommentService service) { this.service = service; }

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping
    public Comments getComments(@PathVariable int adId) { return service.getComments(adId); }

    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping
    public Comment addComment(@PathVariable int adId,
                              @RequestBody @Valid CreateOrUpdateComment dto) {
        return service.addComment(adId, dto);
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        service.deleteComment(adId, commentId);
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable int adId,
                                 @PathVariable int commentId,
                                 @RequestBody @Valid CreateOrUpdateComment dto) {
        return service.updateComment(adId, commentId, dto);
    }
}

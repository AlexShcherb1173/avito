package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;


@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления")
    public CommentsDto getComments(@PathVariable Integer id) {
        return commentService.getCommentsForAd(id);
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению")
    public CommentDto addComment(@PathVariable Integer adId,
                                 @RequestBody CreateOrUpdateCommentDto comment) {
        return commentService.addComment(adId, comment);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновление комментария")
    public String updateComment(@PathVariable Integer adId,
                                @PathVariable Integer commentId,
                                @RequestBody CreateOrUpdateCommentDto comment) {
        commentService.updateComment(adId, commentId, comment);
        return "Комментарий успешно обновлён";
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    public String deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return "Комментарий успешно удалён";
    }
}

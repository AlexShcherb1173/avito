package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer adId) {
        // Логика для получения комментариев объявления
        return new ArrayList<>(); // Возвращаем список комментариев
    }

    @PostMapping
    public CommentDto addComment(@PathVariable Integer adId, @RequestBody CommentDto commentDto) {
        // Логика для добавления комментария
        return commentDto; // Возвращаем добавленный комментарий
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        // Логика для удаления комментария
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CommentDto commentDto) {
        // Логика для обновления комментария
        return commentDto; // Возвращаем обновленный комментарий
    }
}

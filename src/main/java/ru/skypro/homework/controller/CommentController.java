package ru.skypro.homework.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService; // Сервис для работы с комментариями

    @GetMapping
    public Comments getComments(@PathVariable Integer adId) {
        // Логика получения комментариев для объявления
        return commentService.getCommentsForAd(adId);
    }

    @PostMapping
    public Comment addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateComment comment) {
        // Логика добавления комментария к объявлению
        return commentService.addComment(adId, comment);
    }

    @PatchMapping("/{commentId}")
    public String updateComment(@PathVariable Integer adId, @PathVariable Integer commentId,
                                @RequestBody CreateOrUpdateComment comment) {
        // Логика обновления комментария
        commentService.updateComment(adId, commentId, comment);
        return "Комментарий успешно обновлён";
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        // Логика удаления комментария
        commentService.deleteComment(adId, commentId);
        return "Комментарий успешно удалён";
    }
}
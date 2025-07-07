package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.service.CommentsService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    // Получение комментариев объявления
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable("id") int id) throws ObjectNotFoundException {
        return commentsService.getComments(id);
    }

    // Добавление комментария к объявлению
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable("id") int id, @RequestBody CreateOrUpdateComment createdComment) throws ObjectNotFoundException {
        return commentsService.addComment(id, createdComment);
    }

    // Удаление комментария
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId) throws ForbiddenException, ObjectNotFoundException {
        commentsService.deleteComment(adId, commentId);
    }

    // Обновление комментария
    @PatchMapping("/{adId}/comments/{commentId}")
    public Comment updateComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId, @RequestBody CreateOrUpdateComment updatedComment) throws ObjectNotFoundException, ForbiddenException {
        return commentsService.updateComment(adId, commentId, updatedComment);
    }
}

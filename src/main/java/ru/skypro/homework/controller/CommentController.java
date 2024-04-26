package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.rq.comment.Comment;
import ru.skypro.homework.dto.rq.comment.CreateOrUpdateComment;
import ru.skypro.homework.dto.rs.comment.Comments;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

    /**
     * Получение комментариев объявления {@code getComment}
     * @param id комментария
     * @return {@code ResponseEntity.ok(new Comments())} список всех комментариев
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComment(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(new Comments());
    }

    /**
     * Добавление комментария к объявлению {@code postComment}
     * @param id комментария
     * @param createOrUpdateComment комментарий
     * @return {@code ResponseEntity.ok(new Comment())} комментарий
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> postComment(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return ResponseEntity.ok(new Comment());
    }

    /**
     * Удаление комментария {@code deleteComment}
     * @param adId  Id объявления
     * @param commentId Id комментария
     * @return {@code ResponseEntity.ok().build()} код ответа сервера {@code №200,№401,№403,№404}.
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId, @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление комментария {@code updateComment}
     * @param adId  Id объявления
     * @param commentId Id комментария
     * @return {@code ResponseEntity.ok(new Comment())} обновленный комментарий
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("adId") Integer adId, @PathVariable("commentId") Integer commentId, @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return ResponseEntity.ok(new Comment());
    }
}

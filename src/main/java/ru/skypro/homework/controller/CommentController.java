package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.Collections;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class CommentController {

    @GetMapping("/{adId}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer adId) {
        // Заглушка для получения комментариев
        Comments comments = new Comments();
        comments.setCount(0);
        comments.setResults(Collections.emptyList());
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer adId,
            @RequestBody CreateOrUpdateComment comment) {
        // Заглушка для добавления комментария
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(1);
        commentDTO.setText(comment.getText());
        commentDTO.setAuthor(1);
        commentDTO.setAuthorFirstName("Иван");
        commentDTO.setAuthorImage("/images/user1.jpg");
        commentDTO.setCreatedAt(System.currentTimeMillis());
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId) {
        // Заглушка для удаления комментария
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateComment comment) {
        // Заглушка для обновления комментария
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(commentId);
        commentDTO.setText(comment.getText());
        commentDTO.setAuthor(1);
        commentDTO.setAuthorFirstName("Иван");
        commentDTO.setAuthorImage("/images/user1.jpg");
        commentDTO.setCreatedAt(System.currentTimeMillis());
        return ResponseEntity.ok(commentDTO);
    }
}
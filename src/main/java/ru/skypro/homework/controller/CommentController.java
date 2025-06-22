package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CommentsResponseDTO;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    @GetMapping
    public ResponseEntity<CommentsResponseDTO> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(new CommentsResponseDTO());
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Integer adId,
            @RequestBody CommentDto request) {
        return ResponseEntity.ok(new CommentResponseDTO());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody CommentDto request) {
        return ResponseEntity.ok(new CommentResponseDTO());
    }
}

package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class CommentController {
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getAll() {
        return null;
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@RequestBody int id) {
        return null;
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> getInfo(@RequestBody int id, @RequestBody int commentId) {
        return null;
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> changeComment(@RequestBody int id, @RequestBody int commentId) {
        return null;
    }


}

package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.service.CommentService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @PutMapping
    public ResponseEntity<Comment> editComment(@RequestBody List<Comment> comments) {
        return ResponseEntity.ok(new Comment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(new Comment());
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(new ArrayList<Comment>());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import org.springframework.http.HttpStatus;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("ads")
public class CommentController {
    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByAdsId(Integer id) {
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@PathVariable Integer adsId,@PathVariable String text) {
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @DeleteMapping("{adsId}/comments/{commentId}")
    public ResponseEntity<List<CommentDTO>> deleteCommentsByAdsId(Integer adsId, Integer commentId) {
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @PutMapping("{adsId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer adsId,@PathVariable String text) {
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

}
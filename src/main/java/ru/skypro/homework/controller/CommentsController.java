package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentsController {

    private static final Logger log = LoggerFactory.getLogger(CommentsController.class);

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments (@PathVariable Integer id){
        log.info(" " + id);
        Comments comments = new Comments();
        comments.setCount(0);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment (@PathVariable Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment){
        log.info(" " + id);
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        log.info("" + adId + commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateComment createOrUpdateComment){
        log.info(" " + adId + commentId);
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }
}

package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {

    @GetMapping
    public Comments getComments(@PathVariable Integer adId) {
        return new Comments();
    }

    @PostMapping
    public Comment addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateComment comment) {
        return new Comment();
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
    }

    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable Integer adId, @PathVariable Integer commentId,
                                 @RequestBody CreateOrUpdateComment comment) {
        return new Comment();
    }
}

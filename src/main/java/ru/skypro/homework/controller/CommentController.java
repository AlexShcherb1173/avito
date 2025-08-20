package ru.skypro.homework.controller;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.Comment;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Comments", description = "Controller for managing comments on advertisements")
@RestController
@RequestMapping("/ads")
public class CommentController {
    @Operation(summary = "Get comments for an ad", description = "Retrieves all comments for a specific advertisement")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        return new Comments(0, List.of());
    }

    @Operation(summary = "Add a comment to an ad", description = "Creates a new comment for a specific advertisement")
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable int id, @RequestBody Comment comment) {
        return comment;
    }

    @Operation(summary = "Delete a comment by ID", description = "Delete comment by its ID")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable int id, @PathVariable int commentId) {
        
    }
    
    @Operation(summary = "Update a comment by ID", description = "Updates a specific comment for an advertisement")
    @PatchMapping("/{id}/comments/{commentId}")
    public Comment updateComment(@PathVariable int id, @PathVariable int commentId, @RequestBody Comment comment) {
        return comment;
    }
}

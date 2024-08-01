package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentDTO;
import ru.skypro.homework.dto.comments.CommentsDTO;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDTO;

@RestController
@RequestMapping("/ads")
public class CommentsController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentsDTO.class))),
            @ApiResponse(responseCode = "401",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content)

    })

    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDTO> getAllCommentsByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(new CommentsDTO());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "401",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content)
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id,
                                                 @RequestBody CreateOrUpdateCommentDTO comment) {
        return ResponseEntity.ok(new CommentDTO());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content)
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long adId,
                                              @PathVariable Long commentId) {
        return ResponseEntity.ok().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "401",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    content = @Content)
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long adId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CreateOrUpdateCommentDTO comment) {
        return ResponseEntity.ok(new CommentDTO());
    }


}

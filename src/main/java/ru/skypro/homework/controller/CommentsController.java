package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.Collections;

@Tag(name = "Комментарии")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentsController {

    @Operation(summary = "Получение комментариев объявления", operationId = "getComments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comments.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable Integer id) {
        Comments result = new Comments();
        result.setCount(0);
        result.setResults(Collections.emptyList());
        return result;
    }

    @Operation(summary = "Добавление комментария к объявлению", operationId = "addComment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable Integer id,
                              @RequestBody CreateOrUpdateComment request) {
        return new Comment();
    }

    @Operation(summary = "Удаление комментария", operationId = "deleteComment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable Integer adId,
                              @PathVariable Integer commentId) {
    }

    @Operation(summary = "Обновление комментария", operationId = "updateComment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public Comment updateComment(@PathVariable Integer adId,
                                 @PathVariable Integer commentId,
                                 @RequestBody CreateOrUpdateComment request) {
        return new Comment();
    }
}
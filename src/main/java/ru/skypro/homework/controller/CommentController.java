package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateCommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Получить комментарии объявления", tags = "Комментарии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseWrapperCommentDto.class)
                    )}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content})
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperCommentDto> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getCommentsByAdId(id));
    }

    @Operation(summary = "Добавить комментарий к объявлению", tags = "Комментарии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content})
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer id,
                                                 @RequestBody CreateCommentDto createCommentDto,
                                                 Authentication authentication) {
        return ResponseEntity.ok(commentService.addComment(id, createCommentDto, authentication.getName()));
    }

    @Operation(summary = "Удалить комментарий", tags = "Комментарии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content})
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId,
                                           @PathVariable Integer commentId,
                                           Authentication authentication) {
        if (commentService.deleteComment(commentId, authentication.getName())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Обновить комментарий", tags = "Комментарии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content})
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CommentDto commentDto,
                                                    Authentication authentication) {
        return commentService.updateComment(commentId, commentDto, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}
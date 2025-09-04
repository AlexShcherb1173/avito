package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "API для работы с комментариями")
public class CommentsController {

    private final CommentService commentService;

    private static final Logger log = LoggerFactory.getLogger(CommentsController.class);


    @Operation(summary = "Получение комментариев объявления", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comments.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments (@PathVariable Integer id){
        log.info("Получить комментарии для" + id);
        Comments comments = commentService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Добавление комментария к объявлению", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment (@PathVariable Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        log.info("добавление комментария к объявлению:" + id, authentication.name());
        try {
            Comment comment = commentService.addComment(id, createOrUpdateComment, authentication);
            return ResponseEntity.ok(comment);
        }catch (RuntimeException e){
            log.error(" Ошибка добавления комментария к объявлению ", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удаление комментария", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        log.info("удаление комментариев обхявления" + adId + commentId);
        commentService.deleteComment(adId,commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")

    }
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateComment createOrUpdateComment){
        log.info("обновление комментариев " + adId + commentId);
        Comment comment = commentService.updateComment(adId,commentId,createOrUpdateComment);
        return ResponseEntity.ok(comment);
    }
}

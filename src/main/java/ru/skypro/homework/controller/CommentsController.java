package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Комментарии")
public class CommentsController {

    @Autowired
    private final AdServiceImpl adService;
    private final CommentServiceImpl commentService;

    public CommentsController(AdServiceImpl adService, CommentServiceImpl commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    @Operation(summary = "Получение комментариев объявления", tags = {"Комментарии"})
    @PreAuthorize("hasRole('ADMIN) or hasRole('USER') and @commentService.isOwner(#commentId, authentication.name))")
    @GetMapping(path = "/ads/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comments.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> getCommentsAd(@PathVariable("id") int id) {
        log.info("Метод getComments, класса CommentsController. Принято id: {}", id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление комментария к объявлению", tags = {"Комментарии"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @commentService.isOwner(#commentId, authentication.name))")
    @PostMapping(path = "/ads/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentModel.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = ""))
    })
    public ResponseEntity<CommentModel> addCommentsToAd(@PathVariable("id") int id, @RequestBody CommentModel comment) {
        log.info("Метод addComments, класса CommentsController. Принято (int) id: {}", id);
        if (adService.existsById(id)) { // Проверка есть ли объявление или нет
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CommentModel createdComment = commentService.addCommentToAd(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @Operation(summary = "Удаление комментария", tags = {"Комментарии"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') and @commentService.isOwner(#commentId, authentication.name))")
    @DeleteMapping(path = "/ads/{adsId}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> removeComment(@PathVariable("adId") int adId,
                                           @PathVariable("commentId") int commentId,
                                           Authentication authentication) {
        log.info("Метод deleteCommentsAds, класса CommentsController.Приняты (int) adId: {}, (int) commentId: {}",
                adId, commentId);
        if (adService.existsById(adId)) { //Проверка на наличие объявления
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!commentService.existsById(commentId)) { //Проверка на наличие комментария
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        commentService.removeComment(adId, commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление комментария", tags = {"Комментарии"})
    @PatchMapping(path = "/ads/{adId}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentModel.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = ""))
    })
    public ResponseEntity<?> updateComment(@PathVariable("adId") int adId,
                                           @PathVariable("commentId") int commentId,
                                           @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("Метод updateComment, класса CommentsController.Приняты (int) adId: {}, (int) commentId: {}, (object) createOrUpdateComment: {}",
                adId, commentId, createOrUpdateComment);
        return ResponseEntity.ok().build();
    }
}

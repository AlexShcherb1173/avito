package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.service.CommentService;


@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads/{id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final AdController adController;

    public CommentController(CommentService commentService, AdController adController) {
        this.commentService = commentService;
        this.adController = adController;
    }

    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentsDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<CommentsDTO> getAllComments(@PathVariable int id,
                                                      Authentication authentication) {
        ExtendedAdDTO ad = adController.getAds(id, authentication).getBody();

        assert ad != null;
        CommentsDTO commentsDTO = commentService.getComments(ad.getPk());

        return ResponseEntity.ok(commentsDTO);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<CommentDTO> addComment(@PathVariable int id,
                                                 Authentication authentication,
                                                 @RequestBody CreateOrUpdateCommentDTO comment) {

        int adId = adController.getAds(id, authentication).getBody().getPk();

        CommentDTO commentDTO = commentService.addComment(adId, authentication.getName(), comment);

        return ResponseEntity.ok(commentDTO);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("id") int id,
                                                    @PathVariable("commentId") int commentId,
                                                    Authentication authentication,
                                                    @RequestBody CreateOrUpdateCommentDTO comment) {
        int adId = adController.getAds(id, authentication).getBody().getPk();

        CommentDTO commentDTO = commentService.updateComment(adId, commentId, comment);

        return ResponseEntity.ok(commentDTO);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentsDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") int id,
                                                    @PathVariable("commentId") int commentId,
                                                    Authentication authentication) {
        int adId = adController.getAds(id, authentication).getBody().getPk();

        commentService.deleteComment(adId, commentId);

        return ResponseEntity.ok().build();
    }


}

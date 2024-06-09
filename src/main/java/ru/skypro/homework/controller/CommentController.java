package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.config.security.UserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.response.CommentResponse;
import ru.skypro.homework.dto.response.CommentsResponse;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentServiceApi;
import ru.skypro.homework.utils.ValidationUtils;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceApi commentService;
    private final CommentMapper commentMapper;

    @Operation(
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = CommentsResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
            },
            tags = "Комментарии"
    )
    @GetMapping("/{adId}/comments")
    public ResponseEntity<?> getCommentsForAd(@PathVariable Long adId) {
        return ResponseEntity.ok().body(commentMapper.toCommentsResponse(commentService.getCommentsForAd(adId)));
    }

    @Operation(
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
            },
            tags = "Комментарии"
    )
    @PostMapping(value = "/{adId}/comments")
    public ResponseEntity<?> addCommentToAd(
            @PathVariable Long adId,
            @RequestBody @Valid CreateOrUpdateComment createComment,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(
                commentMapper.toCommentResponse(
                        commentService.addCommentToAd(adId, commentMapper.toComment(createComment))
                )
        );

    }

    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Комментарии"
    )
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @commentService.getCommentById(#commentId).user.id")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long adId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        commentService.deleteCommentFromAd(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Комментарии"
    )
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @commentService.getCommentById(#commentId).user.id")
    @PatchMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long adId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid CreateOrUpdateComment createOrUpdateComment,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().body(
                commentMapper.toCommentResponse(commentService.updateComment(adId, commentId, createOrUpdateComment))
        );
    }

}



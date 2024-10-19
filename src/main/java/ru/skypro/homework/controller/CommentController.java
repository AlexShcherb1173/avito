package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@Tag(name = "Комментарии")
@RestController
@RequestMapping("ads")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }


    @Operation(summary = "Добавление комментария к объявлению", tags = "Комментарии",
               responses = {
                       @ApiResponse(
                               responseCode = "200",
                               description = "OK",
                               content = @Content(
                                       mediaType = MediaType.APPLICATION_JSON_VALUE,
                                       schema = @Schema(implementation = Comment.class)
                               )),
                       @ApiResponse(
                               responseCode = "401",
                               description = "Unauthorized"
                       ),
                       @ApiResponse(
                               responseCode = "404",
                               description = "Not found"
                       )
               })
    @PostMapping(value = "/{id}/comments")
    public CommentDTO add(@PathVariable Long id, @Valid Comment comment) {
        return commentService.add(comment);
    }

    @Operation(summary = "Получение комментариев объявления", tags = "Комментарии",
               responses = {
                       @ApiResponse(
                               responseCode = "200",
                               description = "OK",
                               content = @Content(
                                       mediaType = MediaType.APPLICATION_JSON_VALUE,
                                       schema = @Schema(implementation = Comment.class)
                               )),
                       @ApiResponse(
                               responseCode = "401",
                               description = "Unauthorized"
                       ),
                       @ApiResponse(
                               responseCode = "404",
                               description = "Not found"
                       )
               })
    @GetMapping("/{id}/comments")
    public CommentDTO get(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @Operation(summary = "Удаление комментария", tags = "Комментарии",
               responses = {
                       @ApiResponse(
                               responseCode = "200",
                               description = "OK",
                               content = @Content(
                                       mediaType = MediaType.APPLICATION_JSON_VALUE,
                                       schema = @Schema(implementation = Comment.class)
                               )),
                       @ApiResponse(
                               responseCode = "401",
                               description = "Unauthorized"
                       ),
                       @ApiResponse(
                               responseCode = "403",
                               description = "Forbidden"
                       ),
                       @ApiResponse(
                               responseCode = "404",
                               description = "Not Found"
                       )
               })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void delete(@Valid Comment comment) {
        commentService.delete(comment);
    }

    @Operation(summary = "Обновление комментария", tags = "Комментарии",
               responses = {
                       @ApiResponse(
                               responseCode = "200",
                               description = "OK",
                               content = @Content(
                                       mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                       schema = @Schema(implementation = String.class)
                               )),
                       @ApiResponse(
                               responseCode = "401",
                               description = "Unauthorized"
                       )
               })
    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentDTO update(Comment comment) {
        return commentService.update(comment);
    }
}

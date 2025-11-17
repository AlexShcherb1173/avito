package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentControllerInterface {
    @GetMapping("/{id}/comments")
    @Operation(operationId = "getComments", summary = "Получение комментариев объявления", tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    CommentsDto getComments(@PathVariable(name = "id") Integer id);

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    @Operation(operationId = "addComment", summary = "Добавление комментария к объявлению", tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    CommentDto addComment(@PathVariable(name = "id") Integer id, @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto);

    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.findComment(#commentId).author.email == authentication.name")
    @Operation(operationId = "deleteComment", summary = "Удаление комментария", tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    void deleteComment(@PathVariable(name = "adId") Integer adId, @PathVariable(name = "commentId") Integer commentId);

    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.findComment(#commentId).author.email == authentication.name")
    @Operation(operationId = "updateComment", summary = "Обновление комментария", tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    CommentDto updateComment(@PathVariable(name = "adId") Integer adId, @PathVariable(name = "commentId") Integer commentId, @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto);
}

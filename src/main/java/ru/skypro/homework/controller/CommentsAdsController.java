package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsAdsService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "Эндпойнты для работы с пользователями")
@AllArgsConstructor
public class CommentsAdsController {

    private CommentsAdsService service;

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления",
            operationId = "getComments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getCommentsAds(@PathVariable long id) {
        return null;
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению",
            operationId = "addComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> addCommentsAds(@PathVariable long id,
                                            @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    @Operation(summary = "Удаление комментария",
            operationId = "deleteComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> deleteComments(@PathVariable long adId,
                                                 @PathVariable long commentId,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateCommentDto) {
        return null;
    }

    @PatchMapping("/{id}/comments/{commentId}")
    @Operation(summary = "Обновление комментария",
            operationId = "updateComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> updateComments(@PathVariable long adId,
                                                 @PathVariable long commentId,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateCommentDto) {
        return null;
    }

}

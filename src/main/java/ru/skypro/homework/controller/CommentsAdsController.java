package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.impl.CommentsAdsServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "Эндпойнты для работы с комментариями")
@RequiredArgsConstructor
public class CommentsAdsController {

    private final CommentsAdsServiceImpl service;

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления",
            operationId = "getComments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getCommentsAds(@PathVariable Integer id) {
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
    public ResponseEntity<?> addCommentsAds(@PathVariable Integer id,
                                            @RequestBody CreateOrUpdateCommentDto createOrUpdateComment) {
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
    public ResponseEntity<?> deleteComments(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
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
    public ResponseEntity<?> updateComments(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return null;
    }

}

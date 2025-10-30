package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
public class CommentsController {
    private final CommentsServiceImpl commentsService;

    @GetMapping("/{id}/comments")
    @Operation(operationId = "getComments",
            summary = "Получение комментариев объявления",
            tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public Comments getComments(@PathVariable(name = "id") Integer id) {
        return new Comments();
    }

    @PostMapping("/{id}/comments")
    @Operation(operationId = "addComment",
            summary = "Добавление комментария к объявлению",
            tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public Comment addComment(@PathVariable(name = "id") Integer id,
                              @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return new Comment();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(operationId = "deleteComment",
            summary = "Удаление комментария",
            tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public void deleteComment(@PathVariable(name = "adId") Integer adId,
                              @PathVariable(name = "commentId") Integer commentId) {
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(operationId = "updateComment",
            summary = "Обновление комментария",
            tags = {"Комментарии"})
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public Comment updateComment(@PathVariable(name = "adId") Integer adId,
                                 @PathVariable(name = "commentId") Integer commentId,
                                 @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return new Comment();
    }
}

package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads/{adId}/comments")
@Tag(name = "Comments", description = "API для работы с комментариями к объявлениям")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Получение комментариев к объявлению", description = "Возвращает все комментарии к объявлению с пагинацией.")
    @GetMapping
    public CommentsResponse getComments(@PathVariable Long adId) {
        return commentService.getComments(adId);
    }

    @Operation(summary = "Добавление комментария", description = "Оставляет комментарий под объявлением. Автор — текущий пользователь.")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable Long adId,
            @RequestBody @Valid CreateOrUpdateComment commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        return commentService.addComment(adId, userDetails.getUsername(), commentDto);
    }

    @Operation(summary = "Удаление комментария", description = "Удаляет комментарий. Только автор или администратор может удалить.")
    @DeleteMapping("/{commentId}")
    @PreAuthorize("@commentServiceImpl.isCommentAuthor(#commentId, authentication.principal.username) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long adId, @PathVariable Long commentId) {
        commentService.deleteComment(adId, commentId);
    }

    @Operation(summary = "Редактирование комментария", description = "Меняет текст комментария. Только автор может редактировать.")
    @PatchMapping("/{commentId}")
    @PreAuthorize("@commentServiceImpl.isCommentAuthor(#commentId, authentication.principal.username)")
    public CommentDto updateComment(
            @PathVariable Long adId,
            @PathVariable Long commentId,
            @RequestBody @Valid CreateOrUpdateComment dto) {
        return commentService.updateComment(adId, commentId, dto);
    }
}

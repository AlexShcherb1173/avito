package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.CommentService;

/**
 * Контроллер для управления комментариями к объявлениям.
 * Позволяет добавлять, удалять, редактировать и просматривать комментарии.
 */
@RestController
@RequestMapping("/ads/{adId}/comments")
@Tag(name = "Комментарии")
public class CommentsController {

    private final CommentService service;

    /**
     * Конструктор контроллера.
     *
     * @param service сервис для работы с комментариями
     */
    public CommentsController(CommentService service) {
        this.service = service;
    }

    /**
     * Получение всех комментариев к объявлению.
     *
     * @param adId ID объявления
     * @return список комментариев в обёртке Comments
     */
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping
    public Comments getComments(@PathVariable int adId) {
        return service.getComments(adId);
    }

    /**
     * Добавление нового комментария к объявлению.
     *
     * @param adId ID объявления
     * @param dto  данные комментария
     * @return добавленный комментарий
     */
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping
    public Comment addComment(@PathVariable int adId,
                              @RequestBody @Valid CreateOrUpdateComment dto) {
        return service.addComment(adId, dto);
    }

    /**
     * Удаление комментария к объявлению.
     * Доступно только автору комментария или администратору.
     *
     * @param adId      ID объявления
     * @param commentId ID комментария
     */
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        service.deleteComment(adId, commentId);
    }

    /**
     * Обновление текста комментария.
     * Доступно только автору комментария или администратору.
     *
     * @param adId      ID объявления
     * @param commentId ID комментария
     * @param dto       новые данные
     * @return обновлённый комментарий
     */
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    public Comment updateComment(@PathVariable int adId,
                                 @PathVariable int commentId,
                                 @RequestBody @Valid CreateOrUpdateComment dto) {
        return service.updateComment(adId, commentId, dto);
    }
}

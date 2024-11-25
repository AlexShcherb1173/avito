package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

import java.nio.file.AccessDeniedException;

/**
 * <b>Контроллер для обработки запросов, связанных с комментариями к объявлениям. </b> <p>
 *
 * <p>Класс CommentsController предоставляет API для получения, добавления, удаления и обновления комментариев к объявлениям.</p>
 *
 * <p>Обрабатывает следующие запросы:</p>
 * <ul>
 *     <li><code>GET /ads/{id}/comments</code> - получение комментариев для указанного объявления.</li>
 *     <li><code>POST /ads/{id}/comments</code> - добавление нового комментария к указанному объявлению.</li>
 *     <li><code>DELETE /ads/{adId}/comments/{commentId}</code> - удаление комментария по его ID.</li>
 *     <li><code>PATCH /ads/{adId}/comments/{commentId}</code> - обновление существующего комментария.</li>
 * </ul>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentsController {

    private final CommentsServiceImpl service;

    /**
     * <b>Метод для получения комментариев по ID объявления. </b> <p>
     *
     * @param id ID объявления, для которого нужно получить комментарии.
     * @return ResponseEntity с объектом {@link CommentsDTO}, содержащим список комментариев.
     */
    @GetMapping("{id}/comments")
    @Operation(summary = "Получение комментариев объявления", description = "getComments")
    public ResponseEntity<CommentsDTO> getCommentsByListing(@Parameter(description = "ID объявления") @PathVariable long id) {
        return ResponseEntity.ok(service.getComments(id));
    }

    /**
     * <b>Метод для добавления нового комментария к объявлению. </b> <p>
     *
     * @param id ID объявления, к которому добавляется комментарий.
     * @param createOrUpdateComment Объект, содержащий данные нового комментария.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @return ResponseEntity с объектом {@link CommentDTO}, содержащим добавленный комментарий.
     */
    @PostMapping("{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению", description = "addComment")
    public ResponseEntity<CommentDTO> addCommentFromListing(@Parameter(description = "ID объявления") @PathVariable long id,
                                                            @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                            Authentication authentication) {
        return ResponseEntity.ok(service.addComment(id, createOrUpdateComment, authentication));
    }

    /**
     * <b>Метод для удаления комментария по его ID. </b> <p>
     *
     * @param listingId ID объявления, к которому относится комментарий.
     * @param commentId ID комментария, который необходимо удалить.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @throws AccessDeniedException если у пользователя нет прав на удаление комментария.
     * @return ResponseEntity с HTTP статусом 200 (OK) в случае успешного удаления.
     */
    @DeleteMapping("{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария", description = "deleteComment")
    public ResponseEntity<?> deleteComment(@Parameter(description = "ID объявления") @PathVariable long listingId,
                                           @Parameter(description = "ID комментария") @PathVariable long commentId,
                                           Authentication authentication) throws AccessDeniedException {
        service.deleteComment(listingId, commentId, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * <b>Метод для обновления существующего комментария. </b> <p>
     *
     * @param listingId ID объявления, к которому относится комментарий.
     * @param commentId ID комментария, который необходимо обновить.
     * @param createOrUpdateComment Объект, содержащий новые данные для комментария.
     * @param authentication Информация о текущем аутентифицированном пользователе.
     * @throws AccessDeniedException если у пользователя нет прав на обновление комментария.
     * @return ResponseEntity с объектом {@link CommentDTO}, содержащим обновленный комментарий.
     */
    @PatchMapping("{adId}/comments/{commentId}")
    @Operation(summary = "Обновление комментария", description = "updateComment")
    public ResponseEntity<CommentDTO> updateComment(@Parameter(description = "ID объявления") @PathVariable long listingId,
                                                    @Parameter(description = "ID комментария") @PathVariable long commentId,
                                                    @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                    Authentication authentication) throws AccessDeniedException {
        CommentDTO commentDTO = service.updateComment(listingId, commentId, createOrUpdateComment, authentication);
        return ResponseEntity.ok(commentDTO);
    }
}

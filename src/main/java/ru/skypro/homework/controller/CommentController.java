package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;

/**
 * @author Archinski
 */

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Validated
@Slf4j
@Tag(name = "Комментарии", description = "Управление комментариями объявления")
public class CommentController {

    /**
     * Метод для получения всех комментариев объявления
     * @return возвращает список всех комментариев объявления по id объявления
     */
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode="200",
                    description = "OK: возвращает комментарии объявления",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class)))),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: нет доступа к объявлению",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление не найдено",
                    content = @Content
            )
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> getComments(@RequestParam @PathVariable("adId") Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Метод для добавления комментария к объявлению по id объявления
     * @return возвращает добавленный комментарий объявления
     */
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED: комментарий добавлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class)))),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: нет доступа к объявлению",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление найдено",
                    content = @Content
            )
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> postComment(@RequestParam @PathVariable("adId") Integer id,
                                                  @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Метод для удаления объявления
     * @return возвращает статус 200 если комментарий был удален
     */
    @Operation(summary = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: комментарий удален",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED: нет доступа к удалению комментария",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: роль пользователя не предоставляет доступ к данному api",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление или комментарий не найдены",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestParam @PathVariable("adId") Integer adId,
                                           @RequestParam @PathVariable("commentId") Integer commentId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод для обновления комментария по id объявления
     * @return возвращает измененный комментарий
     */
    @Operation(summary = "Обновление комментария")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: комментарий изменен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: роль пользователя не предоставляет доступ к данному api",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление или комментарий не найдены",
                    content = @Content
            )
    })
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@RequestParam @PathVariable("adId") Integer id,
                                                    @RequestParam @PathVariable("commentId") Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

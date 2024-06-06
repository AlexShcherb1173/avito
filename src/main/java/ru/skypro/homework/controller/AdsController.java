package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;
    private final CommentService commentService;

    public AdsController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }


    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений",
            operationId = "getAllAds",
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = AdsDto.class
                    )
                ))
            }

    )
    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            operationId = "addAd",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = @Encoding(
                            name = "properties",
                            contentType = "application/json"
                    ))
                    }
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK" , content = @Content(
                    schema = @Schema(
                            implementation = AdDto.class
                    )
            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }

    )
    //TODO разобраться почему ответ 415 приходит и поправить
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public ResponseEntity<?> addAd(@RequestPart(name = "image") MultipartFile image,
                                   @Valid @RequestPart(name = "properties") CreateOrUpdateAdDto properties) throws IOException {
        AdDto adDto = adService.addAd(image, properties);
        return ResponseEntity.ok(adDto);
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении",
            operationId = "getAds",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ExtendedAdDto.class
                    )
            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления",
            operationId = "removeAd",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') or @adServiceImpl.hasAdAction(#id)")
    public ResponseEntity<?> removeAd(@PathVariable int id) throws IOException {
        if (!adService.removeAd(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении",
            operationId = "updateAds",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(
                                        implementation = CreateOrUpdateAdDto.class
                                )
                            )
                    }
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = AdDto.class
                    )
            )),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') or @adServiceImpl.hasAdAction(#id)")
    public ResponseEntity<AdDto> updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) {
        AdDto adDto = adService.updateAds(id, createOrUpdateAdDto);
        if (adDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(adDto);
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя",
            operationId = "getAdsMe",
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = AdsDto.class
                    )
            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAdsMe() {
        return ResponseEntity.ok(adService.getAdsMe());
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления",
            operationId = "updateImage",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                        )
                    }
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    array = @ArraySchema(
                             schema = @Schema(
                                     type = "string",
                                     format = "byte"
                             )
                    )

            )),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping(value = "/{id}/image" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestParam MultipartFile image) throws IOException {
        byte[] res = adService.updateImage(id, image);
        if (res == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(res);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления",
            operationId = "getComments",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = CommentsDto.class
                    )

            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable int id) {
        CommentsDto commentsDto = commentService.getComments(id);
        if (commentsDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentsDto);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению",
            operationId = "addComment",
            parameters = @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(
                            type = "integer",
                            format = "int32"
                    )
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = CreateOrUpdateCommentDto.class
                            )
                        )
                    }
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                           implementation = CommentsDto.class
                    )
            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PostMapping("/{id}/comments")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public ResponseEntity<CommentDto> addComment(@PathVariable int id,
                                        @Valid @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        CommentDto commentDto = commentService.addComment(id, createOrUpdateCommentDto);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentDto);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария",
            operationId = "deleteComment",
            parameters = {
              @Parameter(
                      name = "adId",
                      in = ParameterIn.PATH,
                      required = true,
                      schema = @Schema(
                              type = "integer",
                              format = "int32"
                      )
              ),
              @Parameter(
                      name = "commentId",
                      in = ParameterIn.PATH,
                      required = true,
                      schema = @Schema(
                              type = "integer",
                              format = "int32"
                      )
              )
            },
            responses = {@ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') or @commentServiceImpl.hasCommentAction(#adId, #commentId)")
    public ResponseEntity<?> deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        CommentDto commentDto = commentService.deleteComment(adId, commentId);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentDto);
    }


    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария",
            operationId = "updateComment",
            parameters = {
                    @Parameter(
                            name = "adId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(
                                    type = "integer",
                                    format = "int32"
                            )
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(
                                    type = "integer",
                                    format = "int32"
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = CreateOrUpdateCommentDto.class
                            )
                    )
            ),
            responses = {@ApiResponse(responseCode = "200", description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = CommentDto.class
                    )
            )),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') or @commentServiceImpl.hasCommentAction(#adId, #commentId)")
    public ResponseEntity<CommentDto> updateComment(@PathVariable int adId,
                                           @PathVariable int commentId,
                                           @Valid @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        CommentDto commentDto = commentService.updateComment(adId, commentId, createOrUpdateCommentDto);
        if (commentDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentDto);
    }


}

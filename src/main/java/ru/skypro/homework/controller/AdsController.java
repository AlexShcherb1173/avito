package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.CommentService;

import java.awt.*;
import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
public class AdsController {
    private final AdService adService;
    private final CommentService commentService;

    public AdsController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    @Operation(summary = "Получение всех объявлений",
            tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class)
                            )
                    )
            })
    @GetMapping()
    public ResponseEntity<Ad> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Добавить объявление",
            tags = "Объявления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateAd.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ads> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                     @RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(adService.addAd(properties, image));
    }

    @Operation(summary = "Получить комментарии объявления",
            tags = "Комментарии",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            })
    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @Operation(summary = "Добавить комментарий к объявлению",
            tags = "Комментарии",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateComment.class)
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            })
    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id, @RequestBody CreateOrUpdateComment createCommentDto) {
        return ResponseEntity.ok(commentService.addComment(id, createCommentDto));
    }

    @Operation(summary = "Получить информацию об объявлении",
            tags = "Объявления",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExtendedAd.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            })
    @GetMapping("{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getAds(id));
    }

    @Operation(summary = "Удалить объявление",
            tags = "Объявления",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
                    ),
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
                            description = "Not found"
                    )
            })
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        if (adService.hasAdAccess(id)) {
            adService.removeAd(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Обновить информацию об объявлении",
            tags = "Объявления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateAd.class)
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            })
    @PatchMapping("{id}")
    public ResponseEntity<Ads> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createAdsDto) {
        if (adService.hasAdAccess(id)) {
            return ResponseEntity.ok(adService.updateDto(id, createAdsDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Удалить комментарий",
            tags = "Комментарии",
            parameters = {
                    @Parameter(
                            name = "adId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
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
                            description = "Not found"
                    )
            })
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        if (commentService.hasCommentAccess(commentId)) {
            commentService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Обновить комментарий",
            tags = "Комментарии",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateOrUpdateComment.class)
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "adId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema()      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
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
                            description = "Not found"
                    )
            })
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody Comment commentDto) {
        if (commentService.hasCommentAccess(commentId)) {
            return ResponseEntity.ok(commentService.updateComment(commentId, commentDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Получить объявления авторизованного пользователя",
            tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Ads.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            })
    @GetMapping("me")
    public ResponseEntity<Ad> getAdsMe() {
        return ResponseEntity.ok(adService.getAdsMe());
    }

    @Operation(summary = "Обновить картинку объявления",
            tags = "Объявления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema()     //TODO
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema      //TODO
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema()      //TODO
                            )
                    ),
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
                            description = "Not found"
                    )
            })
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id, @RequestParam MultipartFile image) throws IOException {
        adService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }


}


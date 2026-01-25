package ru.skypro.homework.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.model.CreateOrUpdateComment;
import ru.skypro.homework.model.ExtendedAd;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Validated
@Tag(name = "Объявления", description = "the Объявления API")
public interface AdsApi {

    default AdsApiDelegate getDelegate() {
        return new AdsApiDelegate() {};
    }

    public static final String PATH_ADD_AD = "/ads";
    /**
     * POST /ads : Добавление объявления
     *
     * @param properties  (required)
     * @param image  (required)
     * @return Created (status code 201)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "addAd",
        summary = "Добавление объявления",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )

    @RequestMapping(
        method = RequestMethod.POST,
        value = AdsApi.PATH_ADD_AD,
        produces = { "application/json" },
        consumes = { "multipart/form-data" }
    )
    
    default ResponseEntity<Ad> addAd(
        @Parameter(name = "properties", description = "", required = true) @Valid @RequestPart(value = "properties", required = true) CreateOrUpdateAd properties,
        @Parameter(name = "image", description = "", required = true) @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        return getDelegate().addAd(properties, image);
    }


    public static final String PATH_ADD_COMMENT = "/ads/{id}/comments";
    /**
     * POST /ads/{id}/comments : Добавление комментария к объявлению
     *
     * @param id  (required)
     * @param createOrUpdateComment  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "addComment",
        summary = "Добавление комментария к объявлению",
        tags = { "Комментарии" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = AdsApi.PATH_ADD_COMMENT,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Comment> addComment(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id,
        @Parameter(name = "CreateOrUpdateComment", description = "") @Valid @RequestBody(required = false) @Nullable CreateOrUpdateComment createOrUpdateComment
    ) {
        return getDelegate().addComment(id, createOrUpdateComment);
    }


    public static final String PATH_DELETE_COMMENT = "/ads/{adId}/comments/{commentId}";
    /**
     * DELETE /ads/{adId}/comments/{commentId} : Удаление комментария
     *
     * @param adId  (required)
     * @param commentId  (required)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "deleteComment",
        summary = "Удаление комментария",
        tags = { "Комментарии" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = AdsApi.PATH_DELETE_COMMENT
    )
    
    default ResponseEntity<Void> deleteComment(
        @NotNull @Parameter(name = "adId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("adId") Integer adId,
        @NotNull @Parameter(name = "commentId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("commentId") Integer commentId
    ) {
        return getDelegate().deleteComment(adId, commentId);
    }


    public static final String PATH_GET_ADS = "/ads/{id}";
    /**
     * GET /ads/{id} : Получение информации об объявлении
     *
     * @param id  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "getAds",
        summary = "Получение информации об объявлении",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = AdsApi.PATH_GET_ADS,
        produces = { "application/json" }
    )
    
    default ResponseEntity<ExtendedAd> getAds(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    ) {
        return getDelegate().getAds(id);
    }


    public static final String PATH_GET_ADS_ME = "/ads/me";
    /**
     * GET /ads/me : Получение объявлений авторизованного пользователя
     *
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "getAdsMe",
        summary = "Получение объявлений авторизованного пользователя",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = AdsApi.PATH_GET_ADS_ME,
        produces = { "application/json" }
    )
    
    default ResponseEntity<Ads> getAdsMe(
        
    ) {
        return getDelegate().getAdsMe();
    }


    public static final String PATH_GET_ALL_ADS = "/ads";
    /**
     * GET /ads : Получение всех объявлений
     *
     * @return OK (status code 200)
     */
    @Operation(
        operationId = "getAllAds",
        summary = "Получение всех объявлений",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = AdsApi.PATH_GET_ALL_ADS,
        produces = { "application/json" }
    )
    
    default ResponseEntity<Ads> getAllAds(
        
    ) {
        return getDelegate().getAllAds();
    }


    public static final String PATH_GET_COMMENTS = "/ads/{id}/comments";
    /**
     * GET /ads/{id}/comments : Получение комментариев объявления
     *
     * @param id  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "getComments",
        summary = "Получение комментариев объявления",
        tags = { "Комментарии" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Comments.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = AdsApi.PATH_GET_COMMENTS,
        produces = { "application/json" }
    )
    
    default ResponseEntity<Comments> getComments(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    ) {
        return getDelegate().getComments(id);
    }


    public static final String PATH_REMOVE_AD = "/ads/{id}";
    /**
     * DELETE /ads/{id} : Удаление объявления
     *
     * @param id  (required)
     * @return No Content (status code 204)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "removeAd",
        summary = "Удаление объявления",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = AdsApi.PATH_REMOVE_AD
    )
    
    default ResponseEntity<Void> removeAd(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    ) {
        return getDelegate().removeAd(id);
    }


    public static final String PATH_UPDATE_ADS = "/ads/{id}";
    /**
     * PATCH /ads/{id} : Обновление информации об объявлении
     *
     * @param id  (required)
     * @param createOrUpdateAd  (optional)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "updateAds",
        summary = "Обновление информации об объявлении",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = AdsApi.PATH_UPDATE_ADS,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Ad> updateAds(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id,
        @Parameter(name = "CreateOrUpdateAd", description = "") @Valid @RequestBody(required = false) @Nullable CreateOrUpdateAd createOrUpdateAd
    ) {
        return getDelegate().updateAds(id, createOrUpdateAd);
    }


    public static final String PATH_UPDATE_COMMENT = "/ads/{adId}/comments/{commentId}";
    /**
     * PATCH /ads/{adId}/comments/{commentId} : Обновление комментария
     *
     * @param adId  (required)
     * @param commentId  (required)
     * @param createOrUpdateComment  (optional)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "updateComment",
        summary = "Обновление комментария",
        tags = { "Комментарии" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = AdsApi.PATH_UPDATE_COMMENT,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Comment> updateComment(
        @NotNull @Parameter(name = "adId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("adId") Integer adId,
        @NotNull @Parameter(name = "commentId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("commentId") Integer commentId,
        @Parameter(name = "CreateOrUpdateComment", description = "") @Valid @RequestBody(required = false) @Nullable CreateOrUpdateComment createOrUpdateComment
    ) {
        return getDelegate().updateComment(adId, commentId, createOrUpdateComment);
    }


    public static final String PATH_UPDATE_IMAGE = "/ads/{id}/image";
    /**
     * PATCH /ads/{id}/image : Обновление картинки объявления
     *
     * @param id  (required)
     * @param image  (required)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     */
    @Operation(
        operationId = "updateImage",
        summary = "Обновление картинки объявления",
        tags = { "Объявления" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/octet-stream", array = @ArraySchema(schema = @Schema(implementation = byte[].class)))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = AdsApi.PATH_UPDATE_IMAGE,
        produces = { "application/octet-stream" },
        consumes = { "multipart/form-data" }
    )
    
    default ResponseEntity<List<byte[]>> updateImage(
        @NotNull @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id,
        @Parameter(name = "image", description = "", required = true) @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        return getDelegate().updateImage(id, image);
    }

}

package ru.skypro.homework.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.CreateOrUpdateAd;
import ru.skypro.homework.model.CreateOrUpdateComment;
import ru.skypro.homework.model.ExtendedAd;

import javax.annotation.Generated;
import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link AdsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public interface AdsApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /ads : Добавление объявления
     *
     * @param properties  (required)
     * @param image  (required)
     * @return Created (status code 201)
     *         or Unauthorized (status code 401)
     * @see AdsApi#addAd
     */
    default ResponseEntity<Ad> addAd(CreateOrUpdateAd properties,
        MultipartFile image) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /ads/{id}/comments : Добавление комментария к объявлению
     *
     * @param id  (required)
     * @param createOrUpdateComment  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#addComment
     */
    default ResponseEntity<Comment> addComment(Integer id,
        CreateOrUpdateComment createOrUpdateComment) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"createdAt\" : 1, \"authorFirstName\" : \"authorFirstName\", \"author\" : 6, \"authorImage\" : \"authorImage\", \"pk\" : 5, \"text\" : \"text\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * DELETE /ads/{adId}/comments/{commentId} : Удаление комментария
     *
     * @param adId  (required)
     * @param commentId  (required)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#deleteComment
     */
    default ResponseEntity<Void> deleteComment(Integer adId,
        Integer commentId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /ads/{id} : Получение информации об объявлении
     *
     * @param id  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#getAds
     */
    default ResponseEntity<ExtendedAd> getAds(Integer id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"image\" : \"image\", \"authorLastName\" : \"authorLastName\", \"authorFirstName\" : \"authorFirstName\", \"phone\" : \"phone\", \"price\" : 6, \"description\" : \"description\", \"pk\" : 0, \"title\" : \"title\", \"email\" : \"email\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /ads/me : Получение объявлений авторизованного пользователя
     *
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     * @see AdsApi#getAdsMe
     */
    default ResponseEntity<Ads> getAdsMe() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"count\" : 0, \"results\" : [ { \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" }, { \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /ads : Получение всех объявлений
     *
     * @return OK (status code 200)
     * @see AdsApi#getAllAds
     */
    default ResponseEntity<Ads> getAllAds() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"count\" : 0, \"results\" : [ { \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" }, { \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /ads/{id}/comments : Получение комментариев объявления
     *
     * @param id  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#getComments
     */
    default ResponseEntity<Comments> getComments(Integer id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"count\" : 0, \"results\" : [ { \"createdAt\" : 1, \"authorFirstName\" : \"authorFirstName\", \"author\" : 6, \"authorImage\" : \"authorImage\", \"pk\" : 5, \"text\" : \"text\" }, { \"createdAt\" : 1, \"authorFirstName\" : \"authorFirstName\", \"author\" : 6, \"authorImage\" : \"authorImage\", \"pk\" : 5, \"text\" : \"text\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * DELETE /ads/{id} : Удаление объявления
     *
     * @param id  (required)
     * @return No Content (status code 204)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not found (status code 404)
     * @see AdsApi#removeAd
     */
    default ResponseEntity<Void> removeAd(Integer id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PATCH /ads/{id} : Обновление информации об объявлении
     *
     * @param id  (required)
     * @param createOrUpdateAd  (optional)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#updateAds
     */
    default ResponseEntity<Ad> updateAds(Integer id,
        CreateOrUpdateAd createOrUpdateAd) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"image\" : \"image\", \"author\" : 6, \"price\" : 5, \"pk\" : 1, \"title\" : \"title\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

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
     * @see AdsApi#updateComment
     */
    default ResponseEntity<Comment> updateComment(Integer adId,
        Integer commentId,
        CreateOrUpdateComment createOrUpdateComment) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"createdAt\" : 1, \"authorFirstName\" : \"authorFirstName\", \"author\" : 6, \"authorImage\" : \"authorImage\", \"pk\" : 5, \"text\" : \"text\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PATCH /ads/{id}/image : Обновление картинки объявления
     *
     * @param id  (required)
     * @param image  (required)
     * @return OK (status code 200)
     *         or Forbidden (status code 403)
     *         or Unauthorized (status code 401)
     *         or Not found (status code 404)
     * @see AdsApi#updateImage
     */
    default ResponseEntity<List<byte[]>> updateImage(Integer id,
        MultipartFile image) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf(""))) {
                    String exampleString = "";
                    ApiUtil.setExampleResponse(request, "", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

package ru.skypro.homework.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;

import javax.annotation.Generated;
import java.util.Optional;

/**
 * A delegate to be called by the {@link UsersApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public interface UsersApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /users/me : Получение информации об авторизованном пользователе
     *
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     * @see UsersApi#getUser
     */
    default ResponseEntity<User> getUser() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"image\" : \"image\", \"role\" : \"USER\", \"phone\" : \"phone\", \"id\" : 0, \"email\" : \"email\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /users/set_password : Обновление пароля
     *
     * @param newPassword  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     * @see UsersApi#setPassword
     */
    default ResponseEntity<Void> setPassword(NewPassword newPassword) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PATCH /users/me : Обновление информации об авторизованном пользователе
     *
     * @param updateUser  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     * @see UsersApi#updateUser
     */
    default ResponseEntity<UpdateUser> updateUser(UpdateUser updateUser) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"phone\" : \"phone\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PATCH /users/me/image : Обновление аватара авторизованного пользователя
     *
     * @param image  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     * @see UsersApi#updateUserImage
     */
    default ResponseEntity<Void> updateUserImage(MultipartFile image) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

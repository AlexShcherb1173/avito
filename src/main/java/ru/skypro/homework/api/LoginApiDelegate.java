package ru.skypro.homework.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import ru.skypro.homework.model.Login;

import javax.annotation.Generated;
import java.util.Optional;

/**
 * A delegate to be called by the {@link LoginApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public interface LoginApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /login : Авторизация пользователя
     *
     * @param login  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     * @see LoginApi#login
     */
    default ResponseEntity<Void> login(Login login) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

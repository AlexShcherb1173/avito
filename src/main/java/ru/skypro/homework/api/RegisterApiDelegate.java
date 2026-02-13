package ru.skypro.homework.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import ru.skypro.homework.model.Register;

import javax.annotation.Generated;
import java.util.Optional;

/**
 * A delegate to be called by the {@link RegisterApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public interface RegisterApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /register : Регистрация пользователя
     *
     * @param register  (optional)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     * @see RegisterApi#register
     */
    default ResponseEntity<Void> register(Register register) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}

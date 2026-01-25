package ru.skypro.homework.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.skypro.homework.model.Register;

import javax.annotation.Generated;
import javax.validation.Valid;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Validated
@Tag(name = "Регистрация", description = "the Регистрация API")
public interface RegisterApi {

    default RegisterApiDelegate getDelegate() {
        return new RegisterApiDelegate() {};
    }

    public static final String PATH_REGISTER = "/register";
    /**
     * POST /register : Регистрация пользователя
     *
     * @param register  (optional)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     */
    @Operation(
        operationId = "register",
        summary = "Регистрация пользователя",
        tags = { "Регистрация" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = RegisterApi.PATH_REGISTER,
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Void> register(
        @Parameter(name = "Register", description = "") @Valid @RequestBody(required = false) @Nullable Register register
    ) {
        return getDelegate().register(register);
    }

}

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
import ru.skypro.homework.model.Login;

import javax.annotation.Generated;
import javax.validation.Valid;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Validated
@Tag(name = "Авторизация", description = "the Авторизация API")
public interface LoginApi {

    default LoginApiDelegate getDelegate() {
        return new LoginApiDelegate() {};
    }

    public static final String PATH_LOGIN = "/login";
    /**
     * POST /login : Авторизация пользователя
     *
     * @param login  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "login",
        summary = "Авторизация пользователя",
        tags = { "Авторизация" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = LoginApi.PATH_LOGIN,
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Void> login(
        @Parameter(name = "Login", description = "") @Valid @RequestBody(required = false) @Nullable Login login
    ) {
        return getDelegate().login(login);
    }

}

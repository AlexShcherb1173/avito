package ru.skypro.homework.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;

import javax.annotation.Generated;
import javax.validation.Valid;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
@Validated
@Tag(name = "Пользователи", description = "the Пользователи API")
public interface UsersApi {

    default UsersApiDelegate getDelegate() {
        return new UsersApiDelegate() {};
    }

    public static final String PATH_GET_USER = "/users/me";
    /**
     * GET /users/me : Получение информации об авторизованном пользователе
     *
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "getUser",
        summary = "Получение информации об авторизованном пользователе",
        tags = { "Пользователи" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = UsersApi.PATH_GET_USER,
        produces = { "application/json" }
    )
    
    default ResponseEntity<User> getUser(
        
    ) {
        return getDelegate().getUser();
    }


    public static final String PATH_SET_PASSWORD = "/users/set_password";
    /**
     * POST /users/set_password : Обновление пароля
     *
     * @param newPassword  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     */
    @Operation(
        operationId = "setPassword",
        summary = "Обновление пароля",
        tags = { "Пользователи" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = UsersApi.PATH_SET_PASSWORD,
        consumes = { "application/json" }
    )
    
    default ResponseEntity<Void> setPassword(
        @Parameter(name = "NewPassword", description = "") @Valid @RequestBody(required = false) @Nullable NewPassword newPassword
    ) {
        return getDelegate().setPassword(newPassword);
    }


    public static final String PATH_UPDATE_USER = "/users/me";
    /**
     * PATCH /users/me : Обновление информации об авторизованном пользователе
     *
     * @param updateUser  (optional)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "updateUser",
        summary = "Обновление информации об авторизованном пользователе",
        tags = { "Пользователи" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUser.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = UsersApi.PATH_UPDATE_USER,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    default ResponseEntity<UpdateUser> updateUser(
        @Parameter(name = "UpdateUser", description = "") @Valid @RequestBody(required = false) @Nullable UpdateUser updateUser
    ) {
        return getDelegate().updateUser(updateUser);
    }


    public static final String PATH_UPDATE_USER_IMAGE = "/users/me/image";
    /**
     * PATCH /users/me/image : Обновление аватара авторизованного пользователя
     *
     * @param image  (required)
     * @return OK (status code 200)
     *         or Unauthorized (status code 401)
     */
    @Operation(
        operationId = "updateUserImage",
        summary = "Обновление аватара авторизованного пользователя",
        tags = { "Пользователи" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = UsersApi.PATH_UPDATE_USER_IMAGE,
        consumes = { "multipart/form-data" }
    )
    
    default ResponseEntity<Void> updateUserImage(
        @Parameter(name = "image", description = "", required = true) @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        return getDelegate().updateUserImage(image);
    }

}

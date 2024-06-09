package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.security.UserPrincipal;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.UserServiceApi;
import ru.skypro.homework.utils.ValidationUtils;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceApi userService;

    private final ImageMapper imageMapper;

    private final UserMapper userMapper;

    private final UpdateUserMapper updateUserMapper;

    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "string"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @PostMapping(path = "set_password", consumes = "application/json")
    public ResponseEntity<?> setPassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid NewPassword password,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.FORBIDDEN);
        }
        userService.changeUserPassword(userPrincipal.getUser(), password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userMapper.toUserResponse(user.getUser()));
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD_REQUEST",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "string"
                                    )
                            )
                    )
            },
            tags = "Пользователи"
    )
    @PatchMapping(path = "me", consumes = "application/json")
    public ResponseEntity<?> updateUserDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid UpdateUser updateUser,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ValidationUtils.createErrorResponse(bindingResult.getAllErrors(), HttpStatus.UNAUTHORIZED);
        }
        User updatedUser  = userService.updateUserDetails(userPrincipal.getUser(), updateUser);
        return ResponseEntity.status(HttpStatus.OK).body(updateUserMapper.fromUser(updatedUser ));
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Файл изображения",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = MultipartFile.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @PatchMapping(path = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Image avatar = imageMapper.toImage(image);
        userService.updateUserAvatar(user.getUser(), avatar);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

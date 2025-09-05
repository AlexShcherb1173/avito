package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "API для управления данными пользователя: просмотр и редактирование профиля")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получение своего профиля",
            description = "Возвращает данные текущего пользователя: имя, фамилия, телефон.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя получены успешно",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
            }
    )
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserDto getUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return userService.getUserDto(userDetails);
    }

    @Operation(
            summary = "Обновление профиля",
            description = "Изменяет имя, фамилию и телефон текущего пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Профиль успешно обновлен",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
            }
    )
    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserDto updateUser(
            @RequestBody @Valid UpdateUser dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return userService.updateUser(dto, userDetails);
    }

    @Operation(
            summary = "Обновление аватара",
            description = "Заменяет аватар текущего пользователя. Файл должен быть изображением.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Файл изображения (JPG, PNG)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            encoding = {
                                    @Encoding(name = "image", contentType = "image/jpeg, image/png")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар успешно обновлён",
                            content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Файл пустой или не изображение"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "413", description = "Файл слишком большой")
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public String updateUserImage(
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        try {
            return userService.updateUserImage(image, userDetails);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user image");
        }
    }

    @Operation(
            summary = "Смена пароля",
            description = "Позволяет пользователю изменить свой пароль. Требуется текущий пароль.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Текущий и новый пароль",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewPassword.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пароль успешно изменён"),
                    @ApiResponse(responseCode = "400", description = "Неверный текущий пароль или слабый новый"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован")
            }
    )
    @PostMapping("/set_password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(
            @RequestBody @Valid NewPassword dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        try {
            userService.setPassword(dto, userDetails);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to set password");
        }
    }
}
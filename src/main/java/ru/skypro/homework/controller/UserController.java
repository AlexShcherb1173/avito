package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.security.Principal;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Validated
@Slf4j
@Tag(name = "Пользователи", description = "Управление данными пользователей")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED: пользователь не авторизован"),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN: нет доступа"),
        @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR: ошибка сервера при обработке запроса")})
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Обновление пароля")
    @ApiResponse(
            responseCode = "200", description = "OK: пароль изменен")
    @PostMapping("/set_password")
    public ResponseEntity setPassword(@RequestBody NewPassword newPassword, Principal principal) {
        LOGGER.info(String.format("Получен запрос для setPassword: newPassword = %s, " + "user = %s", newPassword, principal.getName()));
        userService.setPassword(newPassword, principal);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(
            responseCode = "200", description = "OK: данные пользователя найдены",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDTO.class))
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(Principal principal) {
        LOGGER.info(String.format("Получен запрос для getUser: user = %s", principal.getName()));
        return ResponseEntity.ok().body(userService.getUser(principal));
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(
            responseCode = "200", description = "OK: данные пользователя обновлены",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateUserDTO.class))
    )
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO, Principal principal) {
        LOGGER.info(String.format("Получен запрос для updateUser: updateUserDTO = %s, " + "user = %s", updateUserDTO, principal.getName()));
        return ResponseEntity.ok().body(userService.updateUser(updateUserDTO, principal));
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(
            responseCode = "200", description = "OK: аватар обновлен",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateUserDTO.class))
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserImage(@RequestParam MultipartFile image, Principal principal) {
        LOGGER.info(String.format("Получен запрос для updateUserImage: image = %s, " + "user = %s", image, principal.getName()));
        userService.updateUserImage(image, principal);
        return ResponseEntity.ok().build();
    }
}

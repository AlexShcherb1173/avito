package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserUpdateInfoDTO;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.exceptions.AvatarNotFoundException;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserAvatarServiceImpl;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@Tag(name = "Пользватели")
public class UserController {

    private final UserService userService;
    private final UserAvatarServiceImpl userAvatarServiceImpl;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "201", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PreAuthorize("isAuthenticated()")
    public void setPassword(@RequestBody NewPassword newPassword,
                            Principal principal) {
        userService.setPassword(newPassword, principal);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public UserDTO showMe(Principal principal) {
        log.info("Попытка отображения инфо");
        return userService.showUserInfo(principal);
    }


    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO,
                               Principal principal) {
        userService.updateUserInfo(userUpdateInfoDTO, principal);
    }

    @PatchMapping(path = "/users/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "201", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public void updateAvatar(@NotNull Principal principal, @Parameter(description = "") @Valid @RequestPart(value = "image",
            required = false) MultipartFile file) throws IOException {
        userAvatarServiceImpl.updateAvatar(principal, file);
    }

    @GetMapping("images/{id}")
    public void transferImageToResponse(@PathVariable Long id, HttpServletResponse response) {
        log.info("Попытка отображения ава id");
        try {
            userAvatarServiceImpl.transferImageToResponse(id, response);
        } catch (Exception e) {
            log.error("Ошибка при передаче изображения в ответ", e);
            throw new AvatarNotFoundException("Ошибка при передаче изображения в ответ");
        }
    }
}
package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@Tag(name = "Пользватели")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "201", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")

    public void setPassword(@RequestBody NewPassword newPassword,
                            Principal principal) {
        userService.setPassword(newPassword, principal);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public UserDTO showMe(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Попытка отображения инфо");
        return userService.showUserInfo(userDetails);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public void updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestBody UserDTO userDTO) {

        userService.updateUserInfo(userDetails.getUsername(), userDTO);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "201", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public void uploadImage(@RequestBody LoginDTO loginDTO,
                            @RequestBody MultipartFile multipartFile) throws IOException {
        userService.uploadAvatar(loginDTO, multipartFile);
    }
}

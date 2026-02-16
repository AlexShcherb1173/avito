package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "**",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@Schema(description = "Контроллер для работы с пользователями")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set_password")
    @Operation(summary = "Set new password", description = "Обновление пароля",
        parameters = @Parameter(name = "newPassword", description = "new password"),
        responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "400", description = "Some fields haven't passed validation"),
                @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
    public ResponseEntity<?> setNewPassword(@RequestBody NewPassword newPassword){
        userService.changePassword(newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get user by id", description = "Получение информации об авторизованном пользователе",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
    public ResponseEntity<UserDTO> getMe() {
        UserDTO userDTO = userService.getCurrentUserDto();
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update user", description = "Обновление информации об авторизованном пользователе",
        parameters = @Parameter(name = "updateUser", description = "new user info"),
        responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "400", description = "Some fields haven't passed validation"),
                @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
    public UpdateUser updateMe(@RequestBody UpdateUser updateUser){
        if (updateUser != null) {
            return userService.updateUser(updateUser);
        }
        return null;
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update user image", description = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<Void> updateImage(@RequestParam("image") MultipartFile image) {
        userService.updateUserImage(image);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/me/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Get user image", description = "Получение аватара авторизованного пользователя")
    public ResponseEntity<byte[]> getImage() {
        byte[] image = userService.getUserImage();
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Get user image by id", description = "Получение аватара пользователя по id")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Integer id) {
        byte[] image = userService.getUserImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

}
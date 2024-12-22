package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.CustomUserDetailsManager;

import java.io.IOException;
/**
 * Контроллер для работы с аватарами пользователей.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/avatars")
@Tag(name = "Аватары пользователей")
public class UserAvatarController {
    private final CustomUserDetailsManager userService;
    /**
     * Конструктор контроллера UserAvatarController.
     */
    public UserAvatarController(CustomUserDetailsManager userService) {
        this.userService = userService;
    }
    /**
     * Загрузка аватара пользователя.
     */
    @GetMapping(value = "/{avatarId}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE})
    @Operation(summary = "Загрузка аватара пользователя")
    public ResponseEntity<byte[]> getAvatarFromFs(@PathVariable("avatarId") String avatarId) throws IOException {
        return ResponseEntity.ok().body(userService.getAvatarFromFs(avatarId));
    }
}
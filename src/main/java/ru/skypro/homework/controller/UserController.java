package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Registration.Password;
import ru.skypro.homework.dto.User.UpdatedUser;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.ImageService;
import ru.skypro.homework.service.impl.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи", description = "Операции с пользователями")
@RequestMapping("/users")
public class UserController {


    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal.getName()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(
            Principal principal,
            @RequestBody UpdatedUser updatedUser
    ) {
        return ResponseEntity.ok(userService.updateUser(principal.getName(), updatedUser));
    }

    @Operation(summary = "Обновление пароля пользователя")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(
            Principal principal,
            @RequestBody Password password
    ) {
        userService.updatePassword(principal.getName(), password);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление аватара пользователя")
    @PatchMapping(
            value = "/me/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<Void> updateUserImage(
            Principal principal,
            @RequestPart("image") MultipartFile image) throws IOException {

        userService.updateUserImage(principal.getName(), image);
        return ResponseEntity.ok().build();
    }

    private String saveImage(MultipartFile image, String folder) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;
        Path path = Paths.get("uploads", folder, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return "/" + path.toString().replace("\\", "/");
    }

}




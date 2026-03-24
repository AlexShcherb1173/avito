package ru.avito.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;
import ru.avito.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @PostMapping("/set_password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody NewPasswordRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateUserImageResponse> updateUserImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(userService.updateUserImage(image));
    }
}
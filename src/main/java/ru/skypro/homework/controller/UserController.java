package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.net.Authenticator;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity<Void> passwordUpdate(@RequestBody NewPasswordDto newPasswordDto,
                                               Authentication authentication) {
        userService.updatePassword(newPasswordDto, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication));
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDto> updateUserInfo(@Valid @RequestBody UpdateUserDto userDto,
                                                        Authentication authentication) {
        UpdateUserDto dto = userService.updateUser(userDto, authentication);
        return ResponseEntity.status(200).body(dto);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestPart MultipartFile image,Authentication authentication) {
        userService.updateUserImage(image, authentication);
        return ResponseEntity.ok().build();
    }
}

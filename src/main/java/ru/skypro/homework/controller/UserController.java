package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Методы для получения и изменения информации пользователя")
public class UserController implements UserControllerInterface {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/set_password")
    @PreAuthorize("hasRole('USER')")
    @Override
    public void setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        userServiceImpl.setPassword(newPasswordDto);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Override
    public UserDto getUser() {
        return userServiceImpl.getCurrentUserInformation();
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Override
    public UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return userServiceImpl.updateUserInformation(updateUserDto);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Override
    public void updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
        userServiceImpl.uploadUserPicture(image);
    }
}

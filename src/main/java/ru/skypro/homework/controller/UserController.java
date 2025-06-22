package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.impl.LoggingMethodImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("Method {}", LoggingMethodImpl.getMethodName());
        UserEntity userEntity = userService.getUser(authentication.getName());
        if (userEntity != null) {
            return ResponseEntity.ok(UserMapper.mapperFromUserEntityToUser(userEntity));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser, Authentication authentication) {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        UserEntity userEntity = userService.updateUser(updateUser, authentication);
        if (userEntity != null) {
            return ResponseEntity.ok(UserMapper.mapperFromUserEntityToUpdateUser(userEntity));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/set_password")
    public ResponseEntity<User> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        userService.setPassword(newPassword, authentication);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        userService.updateUserImage(image, authentication);
        return ResponseEntity.ok().build();
    }
}

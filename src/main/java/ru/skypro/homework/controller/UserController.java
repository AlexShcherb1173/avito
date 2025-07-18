package ru.skypro.homework.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Transactional
public class UserController {
    private final UsersRepository userRepository;
    private final ImageRepository imageRepository;
    private final AppMapper appMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(
            @RequestBody NewPassword newPassword,
            Authentication authentication
    ) {
        Users user = (Users) authentication.getPrincipal();

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        Users user = (Users) authentication.getPrincipal();
        User userDto = appMapper.userEntityToUserDTO(user);
        if (user.getImage() != null) {
            userDto.setImage("/images/" + user.getImage().getId());
        }
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(
            @RequestBody UpdateUser updateUser,
            Authentication authentication
    ) {
        Users user = (Users) authentication.getPrincipal();
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateUserImage(
            @RequestParam("image") MultipartFile imageFile,
            Authentication authentication
    ) throws IOException {
        Users user = (Users) authentication.getPrincipal();

        // Удаляем старое изображение
        if (user.getImage() != null) {
            imageRepository.delete(user.getImage());
        }

        // Создаем новое изображение
        Image newImage = new Image();
        newImage.setData(imageFile.getBytes());
        newImage.setMediaType(imageFile.getContentType());
        Image savedImage = imageRepository.save(newImage);

        // Обновляем пользователя
        user.setImage(savedImage);
        userRepository.save(user);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(savedImage.getMediaType()))
                .body(savedImage.getData());
    }
}
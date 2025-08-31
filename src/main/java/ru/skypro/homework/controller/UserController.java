package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/set_password")
    public ResponseEntity<?>
    setPassword(@RequestBody NewPassword newPassword){
        log.info(" Установите новый пароль ");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(){
        log.info(" Получить пользователя по имени");
        User user = new User();
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser){
        log.info(" Обновить пользователя по имени ");
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<?> updateUserImage(@RequestParam ("/image") MultipartFile image){
        log.info(" Обновить изображение пользователя ");
        return ResponseEntity.ok().build();
    }
}

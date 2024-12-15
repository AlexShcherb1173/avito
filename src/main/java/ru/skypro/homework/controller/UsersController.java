package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;


import javax.validation.Valid;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {


    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword) {
        return ResponseEntity.status(
                (newPassword!=null) ?
                        HttpStatus.OK :
                        HttpStatus.UNAUTHORIZED
        ).build();

    }

    @GetMapping("/me")
    public User getUser() {
        User user = null;
        return user;
    }

    @PatchMapping("/me")
    public void updateUser(@Valid @RequestBody UpdateUser updateUser) {
       // userService.updateUser(updateUser);
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUserImage(@RequestParam() MultipartFile image)  {
      //  userService.updateImage(image);
    }
}
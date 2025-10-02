package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public void setPassword(@RequestBody UserDto userDto) {
    }

    @GetMapping("/me")
    public UserDto getUser() {
        UserDto userDto = new UserDto();
        return userDto;
    }

    @PatchMapping("/me")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userDto;
    }

    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image") String image) {
    }
}
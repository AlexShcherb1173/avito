package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Announcemenst;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/set_password")
    public ResponseEntity<?> changePassword(@RequestBody Login login) {
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> get(@RequestBody Login login) {
        return null;
    }

    @PatchMapping("/me")
    public ResponseEntity<?> newInfoUser(@RequestBody Register register) {
        return null;
    }

    @PatchMapping("/me/image")
    public ResponseEntity<?> newImage(@RequestBody Announcemenst announcemenst) {
        return null;
    }
}

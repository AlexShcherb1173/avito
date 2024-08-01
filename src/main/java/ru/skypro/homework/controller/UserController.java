package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.User.NewPasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.awt.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository repository;

    private UserMapper mapper;

    public UserController(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @PostMapping
    public NewPasswordDTO  set_password(){}
    @GetMapping
    public UserDTO me(){

    }
    @PatchMapping
    private UpdateUserDTO me(){}
    @PatchMapping("/me")
    private Image image(){}
}

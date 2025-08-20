package ru.skypro.homework.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import ru.skypro.homework.service.UsersService;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.dto.request.SetPasswordRequest;
import ru.skypro.homework.dto.request.UpdateUserRequest;
import ru.skypro.homework.dto.response.UpdateUserResponse;
import ru.skypro.homework.dto.User;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@Tag(name = "Users", description = "Controller for managing user profiles")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }


    @Operation(summary = "Set password", description = "Check old password and set new password")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/set_password")
    public void setPassword(@RequestBody SetPasswordRequest request) {
        
    }

    @Operation(summary = "Get user information", description = "Retrieves the user's profile information")
    @GetMapping("/me")
    public UserResponse getUser() {
    UserResponse userResponse = usersService.getInfo();
    return new UserResponse();
}
    @Operation(summary = "Update user information", description = "Updates the user's profile information")
    @PatchMapping("/me")
    public UpdateUserResponse updateUser(@RequestBody UpdateUserRequest updateUser) {
        return usersService.updateUser(updateUser);
    }

    @Operation(summary = "Update user image", description = "Update the user's profile image")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me/image")
    public void updateUserImage(@RequestParam String image) {
        
    }
}

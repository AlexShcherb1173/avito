package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for user login")
@Data
public class Login {

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
    
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}

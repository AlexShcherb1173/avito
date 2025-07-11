package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO пользователя")
public class User {
    @Schema(description = "ID пользователя")
    private Integer id;

    @Schema(description = "Email (логин) пользователя")
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String image;
}
package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String image;
}

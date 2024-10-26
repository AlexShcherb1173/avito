package ru.skypro.homework.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class User {

    private Integer id;

    private String email;

    private String firstname;

    private String lastName;

    private String phone;

    private String role;

    private String image;
}

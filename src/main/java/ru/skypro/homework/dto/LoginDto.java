package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @Size(min = 8, max = 16)
    private String password;

    @Email
    @Size(min = 4, max = 32)
    private String username;
}

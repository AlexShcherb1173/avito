package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserDto {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    // type String и дальше указан энам. Как тут отписать поле?
    private Role role;
    private String image;
}

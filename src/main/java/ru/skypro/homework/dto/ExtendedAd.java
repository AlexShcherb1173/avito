package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAd {

    private Integer pk;

    private String authorFirstName;

    private String authorLastName;

    private String description;

    private Integer price;

    private String email;

    private String image;

    private UserDto author;

    private String phone;


    private String title;
}
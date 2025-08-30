package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAd {
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    private String image;
    private User author;
}
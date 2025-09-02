package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdDto {
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    private String image;
}
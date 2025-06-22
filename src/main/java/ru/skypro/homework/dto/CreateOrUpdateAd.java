package ru.skypro.homework.dto;

import lombok.*;
@Data
@ToString
public class CreateOrUpdateAd {
    private Long id;
    private String description;
    private String image;
    private Integer price;
    private String title;
    
}

package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdDTO {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}
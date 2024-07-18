package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AdDto {

    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}

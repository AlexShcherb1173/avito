package ru.skypro.homework.dto;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AdDto {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;

}

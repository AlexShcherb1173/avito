package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {

    private Long author;
    private String image;
    private Integer pk;
    private Double price;
    private String title;
}

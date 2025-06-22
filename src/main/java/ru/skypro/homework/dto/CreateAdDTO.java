package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateAdDTO {
    private String title;
    private Integer price;
    private String description;
}

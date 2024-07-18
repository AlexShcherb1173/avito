package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateOrUpdateAdDto {

    private String title;
    private int price;
    private String description;
}

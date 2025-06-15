package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {

    private Integer cont; // Общее количество объявлений
    private List<Ad> results;

}

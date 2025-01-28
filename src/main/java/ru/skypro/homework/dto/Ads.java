package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    /*
    общее количество объявлений
     */
    private Integer count;


    private List<Ad> results;
}

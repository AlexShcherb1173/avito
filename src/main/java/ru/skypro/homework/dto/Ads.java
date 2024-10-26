package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    //                                      Все объявления (DTO)
    private int count;                  //  Общее количество объявлений
    private List<Ad> results;     //  Коллекция объявлений

}

package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class Ads {
    //                                      Все объявления (DTO)
    private int count;                  //  Общее количество объявлений
    private Collection<Ad> results;     //  Коллекция объявлений

}

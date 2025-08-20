package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;
@Data
public class Ad {
    private int count;
    private List<Ads> results;
}

package ru.skypro.homework.dto.ads;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    int count;
    List<Ad> results;
}

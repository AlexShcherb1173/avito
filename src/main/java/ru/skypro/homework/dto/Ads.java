package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Ads {
    @JsonProperty("count")
    private int count;	// общее количество объявлений
    @JsonProperty("results")
    private List<Ad> results; // список объявлений

    public Ads() {
        this.count = 0;
        this.results = new ArrayList<>();
    }

    public void addAd(Ad ad) {
        results.add(ad);
        count = results.size();
    }
}


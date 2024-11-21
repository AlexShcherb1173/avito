package ru.skypro.homework.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Ads {
    private Integer count;
    private List<Ad> results;

    public Ads(Integer count, List<Ad> results) {
        this.count = count;
        this.results = results;
    }
    public Ads() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Ad> getResults() {
        return results;
    }

    public void setResults(List<Ad> results) {
        this.results = results;
    }
}

package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Ads {
    @Schema(type = "integer", format = "int32", description = "общее количество объявлений")
    private Integer count;

    @Schema(description = "Список объявлений")
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

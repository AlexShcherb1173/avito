package ru.skypro.homework.dto;

import java.util.List;
import java.util.Objects;

public class Ads {
    private Integer count;
    private List<Ad> results;


    public Integer getCount() {
        return count;
    }

    public List<Ad> getResults() {
        return results;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setResults(List<Ad> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(count, ads.count) && Objects.equals(results, ads.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, results);
    }

    @Override
    public String toString() {
        return "Ads{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}

package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class Ads {

    private Integer cont; // Общее количество объявлений
    private List<Ad> results;

    public Ads(Integer cont, List<Ad> results) {
        this.cont = cont;
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(cont, ads.cont) && Objects.equals(results, ads.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cont, results);
    }

    @Override
    public String toString() {
        return "Ads{" +
                "cont=" + cont +
                ", results=" + results +
                '}';
    }
}

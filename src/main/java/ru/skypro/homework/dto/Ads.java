package ru.skypro.homework.dto;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class Ads {
    private int count;
    @OneToMany(mappedBy = "results", fetch = FetchType.EAGER)
    private List<Ad> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Ad> getResults() {
        return results;
    }

    public void setResults(List<Ad> results) {
        this.results = results;
    }
}

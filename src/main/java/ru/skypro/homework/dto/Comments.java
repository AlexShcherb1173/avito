package ru.skypro.homework.dto;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class Comments {
    private int count;
    @OneToMany(mappedBy = "results", fetch = FetchType.EAGER)
    private List<Comment> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getResults() {
        return results;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }
}

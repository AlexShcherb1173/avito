package ru.skypro.homework.dto;

import java.util.List;

public class Comments {
    private Integer count;
    private List<Comment> results;

    public Comments() {}

    public Comments(Integer count, List<Comment> results) {
        this.count = count;
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public List<Comment> getResults() {
        return results;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }
}
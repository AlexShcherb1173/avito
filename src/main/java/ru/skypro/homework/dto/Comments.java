package ru.skypro.homework.dto;

import java.util.Arrays;
import java.util.Objects;

public class Comments {
    private Integer count;
    private Ad[] results;

    public Comments(Integer count, Ad[] results) {
        this.count = count;
        this.results = results;
    }
    public Comments() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Ad[] getResults() {
        return results;
    }

    public void setResults(Ad[] results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return Objects.equals(count, comments.count) && Arrays.equals(results, comments.results);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(count);
        result = 31 * result + Arrays.hashCode(results);
        return result;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "count=" + count +
                ", results=" + Arrays.toString(results) +
                '}';
    }
}

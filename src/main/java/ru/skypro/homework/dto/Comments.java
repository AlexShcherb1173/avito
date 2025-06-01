package ru.skypro.homework.dto;

import lombok.Data;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Objects;

@Data
public class Comments {

    private Integer count; // Общее количество комментариев
    private List<Comment> results; // Список комментариев

    public Comments(Integer count, List<Comment> results) {
        this.count = count;
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return Objects.equals(count, comments.count) && Objects.equals(results, comments.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, results);
    }

    @Override
    public String toString() {
        return "Comments{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}

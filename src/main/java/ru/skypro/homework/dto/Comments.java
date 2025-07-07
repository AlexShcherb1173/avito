package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Comments {
    @JsonProperty("count")
    private int count;	// общее количество комментариев
    @JsonProperty("results")
    private List<Comment> results;	// список комментариев

    public Comments() {
        this.count = 0;
        this.results = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        results.add(comment);
        count = results.size();
    }
}

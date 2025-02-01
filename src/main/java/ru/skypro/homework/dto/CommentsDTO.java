package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "Комментарии")
public class CommentsDTO {
    @Schema(description = "Общее количество комментариев")
    private int count;
    @Schema(description = "Список комментариев")
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

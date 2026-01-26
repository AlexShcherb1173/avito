package ru.skypro.homework.dto;

import java.util.List;

public class CommentsResponseDto {
    private Integer count;
    private List<CommentResponseDto> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CommentResponseDto> getResults() {
        return results;
    }

    public void setResults(List<CommentResponseDto> results) {
        this.results = results;
    }
}

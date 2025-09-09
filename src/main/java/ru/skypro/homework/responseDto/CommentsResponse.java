package ru.skypro.homework.responseDto;

/**
 * описывает структуру ответа при получении комментариев к объявлению
  */


import lombok.Data;

import java.util.List;

@Data
public class CommentsResponse {
    private Integer count;
    private List<CommentDto> results;

    public CommentsResponse(Integer count, List<CommentDto> results) {
        this.count = count;
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CommentDto> getResults() {
        return results;
    }

    public void setResults(List<CommentDto> results) {
        this.results = results;
    }
}
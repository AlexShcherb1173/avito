package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "List of comments")
public class CommentsDto {
    @Schema(description = "Total count")
    private Integer count;

    @Schema(description = "Results")
    private java.util.List<CommentDto> results;

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public java.util.List<CommentDto> getResults() { return results; }
    public void setResults(java.util.List<CommentDto> results) { this.results = results; }
}

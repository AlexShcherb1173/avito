package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "List of comments")
public class Comments {
    @Schema(description = "Total count")
    private Integer count;

    @Schema(description = "Results")
    private java.util.List<Comment> results;

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public java.util.List<Comment> getResults() { return results; }
    public void setResults(java.util.List<Comment> results) { this.results = results; }
}

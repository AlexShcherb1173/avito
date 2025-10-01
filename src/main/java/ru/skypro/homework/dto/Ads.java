package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "List of ads")
public class Ads {
    @Schema(description = "Total count")
    private Integer count;

    @Schema(description = "Results")
    private java.util.List<Ad> results;

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public java.util.List<Ad> getResults() { return results; }
    public void setResults(java.util.List<Ad> results) { this.results = results; }
}

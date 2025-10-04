package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для представления списка объявлений.
 * Содержит общее количество и список DTO объявлений.
 */
@Schema(description = "List of ads")
public class AdsDto {
    @Schema(description = "Total count")
    private Integer count;

    @Schema(description = "Results")
    private java.util.List<AdDto> results;

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public java.util.List<AdDto> getResults() { return results; }
    public void setResults(java.util.List<AdDto> results) { this.results = results; }
}
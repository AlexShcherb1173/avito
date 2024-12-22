package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Objects;
/**
 * DTO (Data Transfer Object) для списка объявлений.
 * <p>
 * В классе представлены данные об объявлениях, а именно: количество и сам список.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
public class AdListDto {

    @Schema(type = "integer",
            format = "int32",
            description = "общее количество объявлений")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<AdDto> results;

    @Override
    public String toString() {
        return "Ads{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdListDto adListDto = (AdListDto) o;
        return Objects.equals(count, adListDto.count) && Objects.equals(results, adListDto.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, results);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<AdDto> getResults() {
        return results;
    }

    public void setResults(List<AdDto> results) {
        this.results = results;
    }

    public AdListDto() {
    }

    public AdListDto(Integer count, List<AdDto> results) {
        this.count = count;
        this.results = results;
    }
}




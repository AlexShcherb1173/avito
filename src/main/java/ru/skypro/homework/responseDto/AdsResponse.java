package ru.skypro.homework.responseDto;

/**
 *  как будет выглядеть JSON-ответ при запросах к эндпоинтам, которые возвращают список объявлений с пагинацией, например:
 * GET /ads — все объявления
 * GET /ads/me — мои объявления
 */

import lombok.Data;

import java.util.List;

@Data
public class AdsResponse {
    private Integer count;
    private List<AdDto> results;

    public AdsResponse(Integer count, List<AdDto> results) {
        this.count = count;
        this.results = results;
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
}
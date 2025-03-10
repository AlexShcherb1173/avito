package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdsDto {
    @Schema(description = "общее количество объявлений")
//    int count;
//
//    List<AdDto> results=new ArrayList<>();
//

//    public AdsDto addResultsItem(AdDto resultsItem) {
//        if (this.results == null) {
//            this.results = new ArrayList<>();
//        }
//        this.results.add(resultsItem);
//        return this;
//    }
    private Integer count;


    private List<AdDto> results;
}

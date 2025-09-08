package ru.skypro.homework.dto.ads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdsDto {
    private int count;

    private List<AdDto> results;
}

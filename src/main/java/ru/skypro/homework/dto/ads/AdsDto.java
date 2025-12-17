package ru.skypro.homework.dto.ads;

import lombok.Data;
import java.util.List;

@Data
public class AdsDto {
    int count;
    List<AdDto> results;
}

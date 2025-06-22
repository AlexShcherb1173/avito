package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Data
@Getter
@Setter
public class AdsResponseDTO {
    private Integer count;
    private List<AdResponseDTO> results = Collections.singletonList(new AdResponseDTO());

}

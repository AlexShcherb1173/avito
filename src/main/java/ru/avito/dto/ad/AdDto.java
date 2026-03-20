package ru.avito.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDto {

    private Integer pk;
    private Integer author;
    private String title;
    private Integer price;
    private String image;
}
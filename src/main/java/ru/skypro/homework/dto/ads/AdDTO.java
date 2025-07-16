package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.PrimitiveIterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}

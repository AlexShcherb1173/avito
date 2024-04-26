package ru.skypro.homework.dto.rs.ad;

import lombok.Data;
import ru.skypro.homework.dto.rs.ad.Ad;

import java.util.List;

@Data
public class Ads {
    private Integer count;
    private List<Ad> results;
}

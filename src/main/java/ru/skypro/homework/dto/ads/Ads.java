package ru.skypro.homework.dto.ads;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Ads {

    private Integer count;
    private List<AdDTO> results;

    public Ads(List<AdDTO> results) {
        this.results = results;
        this.count = results.size();
    }
}

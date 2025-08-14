package ru.skypro.homework.dto.ads;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {

    private Integer author;

    private String image;

    private Integer pk;

    private Integer price;

    private String title;
}

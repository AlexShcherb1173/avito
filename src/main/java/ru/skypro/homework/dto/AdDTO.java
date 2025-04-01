package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.Image;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Ad")
@Data
public class AdDTO {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

    public AdDTO(String title) {
        this.title = title;
    }

    public AdDTO(String testAd, int i, String testDescription) {

    }
}

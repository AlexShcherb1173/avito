package ru.skypro.homework.dto.Advertisement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Краткая информация об объявлении")
public class Ad {

    @Schema(description = "ID автора объявления")
    private Integer author = 0;

    @Schema(description = "Ссылка на изображение",
            example = "/ads/image/1")
    private String image = "";

    @Schema(description = "ID объявления")
    private Integer pk = 0;

    @Schema(description = "Цена в рублях")
    private Integer price = 0;

    @Schema(description = "Заголовок объявления")
    private String title = "";
}

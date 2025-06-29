package ru.skypro.homework.dto.Advertisement;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.L;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация об объявлении")
public class AdDTO {

    @Schema(description = "ID автора объявления")
    private Long author ;

    @Schema(description = "Ссылка на изображение",
            example = "/ads/image/1")
    private String image = "";

    @Schema(description = "ID объявления")
    private Long pk ;

    @Schema(description = "Цена в рублях")
    private Integer price = 0;

    @Schema(description = "Заголовок объявления")
    private String title = "";
}

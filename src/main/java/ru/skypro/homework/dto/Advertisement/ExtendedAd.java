package ru.skypro.homework.dto.Advertisement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Полная информация об объявлении
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Полная информация об объявлении")
public class ExtendedAd {

    @Schema(description = "ID объявления")
    private Long pk ;

    @Schema(description = "Имя автора")
    private String authorFirstName = "";

    @Schema(description = "Фамилия автора")
    private String authorLastName = "";

    @Schema(description = "Подробное описание")
    private String description = "";

    @Schema(description = "Email автора")
    private String email = "";

    @Schema(description = "Ссылка на изображение")
    private String image = "";

    @Schema(description = "Телефон автора")
    private String phone = "";

    @Schema(description = "Цена в рублях")
    private Integer price = 0;

    @Schema(description = "Заголовок объявления")
    private String title = "";
}

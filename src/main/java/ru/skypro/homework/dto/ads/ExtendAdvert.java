package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий расширенные данные объявления.
 * <p>
 * Этот класс {@code ExtendedAd} используется для хранения детальной информации об объявлениях.
 * Он включает в себя всю необходимую информацию, такую как данные автора, описание, контактную информацию,
 * изображение, цену и заголовок объявления.
 * Предназначен для использования в сценариях, где требуется полная информация об объявлении.
 * </p>
 *
 * @author Archinski
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendAdvert {

    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;
}

package ru.skypro.homework.dto.listing;

import lombok.*;

/**
 * <b>Класс ExtendedListingDTO представляет собой объект передачи данных (DTO),
 * который используется для хранения информации о товаре или услуге в системе. </b> <p>
 *
 * <p>Этот класс содержит информацию об авторе объявления, его описании,
 * контактных данных и цене.</p>
 *
 * <p>Используется в контексте работы с объявлениями в приложении.</p>
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedListingDTO {
    /**
     * <b>Уникальный идентификатор объявления. </b> <p>
     */
    private Long pk;

    /**
     * <b>Имя автора объявления. </b> <p>
     */
    private String authorFirstName;

    /**
     * <b>Фамилия автора объявления. </b> <p>
     */
    private String authorLastName;

    /**
     * <b>Описание товара или услуги. </b> <p>
     */
    private String description;

    /**
     * <b>Электронная почта автора объявления для связи. </b> <p>
     */
    private String email;

    /**
     * <b>URL изображения товара или услуги. </b> <p>
     */
    private String image;

    /**
     * <b>Номер телефона автора объявления для связи. </b> <p>
     */
    private String phone;

    /**
     * <b>Цена товара или услуги. </b> <p>
     */
    private Integer price;

    /**
     * <b>Заголовок объявления. </b> <p>
     */
    private String title;

}



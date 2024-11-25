package ru.skypro.homework.dto.listing;

import lombok.*;

import java.util.List;

/**
 * <b>Класс ListingsDTO представляет собой объект передачи данных (DTO),
 * который используется для хранения информации о списке объявлений. </b> <p>
 *
 * <p>Этот класс содержит общее количество объявлений и список
 * конкретных объявлений, представленных в виде объектов {@link ListingDTO}.</p>
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingsDTO {

    /**
     * <b>Общее количество объявлений в списке. </b> <p>
     */
    private int count;

    /**
     * <b>Список объектов ListingDTO, представляющих отдельные объявления. </b> <p>
     */
    private List<ListingDTO> results;

}

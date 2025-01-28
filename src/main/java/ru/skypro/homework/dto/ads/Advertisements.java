package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс-контейнер для группы DTO объявлений.
 * <p>
 * Класс {@code Ads} предназначен для удобной упаковки и передачи нескольких DTO объявлений,
 * {@code AdDTO}, а также для предоставления дополнительной информации, такой как общее количество
 * объявлений в этом наборе.
 * </p>
 * <p>
 * Этот класс полезен для сценариев, где требуется передать список объявлений, например, при пагинации
 * или в ответах API, где необходимо вместе со списком предоставить дополнительные метаданные.
 * </p>
 *
 * @author Archinski
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisements {

    private Integer count;

    private List<AdvertisementsDTO> results;
}


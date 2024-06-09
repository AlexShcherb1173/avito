package ru.skypro.homework.service;

import ru.skypro.homework.model.Image;

public interface ImageServiceApi {

    /**
     * Получить картинку по id
     *
     * @param id картинки
     * @return {@link Image}
     */
    Image getImageById(Long id);

}

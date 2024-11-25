package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

import java.io.IOException;

/**
 * Интерфейс ImageService предоставляет методы для работы с изображениями.
 * Он включает операции для загрузки, удаления и получения изображений.
 */
public interface ImageService {

    /**
     * <b>Загружает изображение и связывает его с указанным идентификатором. </b> <p>
     *
     * @param id идентификатор сущности, к которой будет привязано изображение
     * @param imageFile файл изображения, который нужно загрузить
     * @return объект Image, представляющий загруженное изображение
     * @throws IOException если произошла ошибка при загрузке файла
     */
    Image uploadImage(long id, MultipartFile imageFile) throws IOException;

    /**
     * <b>Удаляет указанное изображение. </b> <p>
     *
     * @param image объект Image, который нужно удалить
     */
    void removeImage(Image image);

    /**
     * <b>Получает изображение по его идентификатору. </b> <p>
     *
     * @param id идентификатор изображения, которое нужно получить
     * @return объект Image, представляющий запрашиваемое изображение
     */
    Image getImage(Long id);
}

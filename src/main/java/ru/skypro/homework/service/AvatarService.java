package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;

import java.io.IOException;

/**
 * Интерфейс для сервисов управления аватарами пользователей.
 * Предоставляет методы для загрузки, удаления и получения аватаров.
 */
public interface AvatarService {

    /**
     * <b>Загружает аватар пользователя. </b> <p>
     *
     * @param image файл изображения, представляющий аватар
     * @return объект {@link Avatar}, представляющий загруженный аватар
     * @throws IOException если произошла ошибка при загрузке файла
     */
    Avatar uploadAvatar(MultipartFile image) throws IOException;

    /**
     * <b>Удаляет аватар пользователя. </b> <p>
     *
     * @param avatar объект {@link Avatar}, который необходимо удалить
     */
    void removeAvatar(Avatar avatar);

    /**
     * <b>Получает аватар по его идентификатору. </b> <p>
     *
     * @param id идентификатор аватара
     * @return объект {@link Avatar}, соответствующий указанному идентификатору,
     *         или null, если аватар не найден.
     */
    Avatar getAvatar(Long id);
}

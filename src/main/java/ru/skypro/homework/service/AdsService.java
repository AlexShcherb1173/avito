package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;

/**
 * Сервис для управления объявлениями.
 * Предоставляет полный CRUD функционал для работы с объявлениями,
 * включая управление изображениями и проверку прав доступа.
 */
public interface AdsService {

    /**
     * Получает список всех объявлений.
     *
     * @return объект {@link Ads} с общим количеством и списком объявлений
     */
    Ads getAllAds();

    /**
     * Создает новое объявление.
     * Прикрепляет изображение, если оно предоставлено.
     *
     * @param properties данные объявления
     * @param image изображение объявления (опционально)
     * @param username email автора объявления
     * @return созданное объявление
     * @throws javax.persistence.EntityNotFoundException если пользователь не найден
     */
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, String username);

    /**
     * Получает расширенную информацию об объявлении.
     * Включает данные автора и детальную информацию об объявлении.
     *
     * @param id идентификатор объявления
     * @return объект {@link ExtendedAd} с полной информацией
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     */
    ExtendedAd getExtendedAd(Integer id);

    /**
     * Удаляет объявление.
     * Доступ разрешен только администраторам или владельцам объявления.
     * Удаляет связанное изображение из файловой системы.
     *
     * @param id идентификатор объявления
     * @param username email пользователя, выполняющего операцию
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     * @throws SecurityException если пользователь не имеет прав на удаление
     */
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.isAdOwner(#id, authentication.name)")
    void removeAd(Integer id, String username);

    /**
     * Обновляет информацию об объявлении.
     * Доступ разрешен только администраторам или владельцам объявления.
     *
     * @param id идентификатор объявления
     * @param updateAd обновленные данные
     * @param username email пользователя, выполняющего операцию
     * @return обновленное объявление
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     * @throws SecurityException если пользователь не имеет прав на обновление
     */
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.isAdOwner(#id, authentication.name)")
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd, String username);

    /**
     * Получает список объявлений конкретного пользователя.
     *
     * @param username email пользователя
     * @return объект {@link Ads} с объявлениями пользователя
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     */
    Ads getAdsByUser(String username);

    /**
     * Обновляет изображение объявления.
     * Доступ разрешен только администраторам или владельцам объявления.
     * Старое изображение удаляется при загрузке нового.
     *
     * @param id идентификатор объявления
     * @param image новое изображение
     * @param username email пользователя, выполняющего операцию
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     * @throws SecurityException если пользователь не имеет прав на обновление
     */
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.isAdOwner(#id, authentication.name)")
    void updateAdImage(Integer id, MultipartFile image, String username);

    /**
     * Получает изображение объявления в виде массива байтов.
     *
     * @param id идентификатор объявления
     * @return массив байтов изображения
     * @throws javax.persistence.EntityNotFoundException если объявление или изображение не найдены
     */
    byte[] getAdImage(Integer id);

    /**
     * Получает сущность объявления по идентификатору.
     * Используется для внутренних операций, требующих доступ к сущности.
     *
     * @param id идентификатор объявления
     * @return сущность {@link AdEntity}
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     */
    AdEntity getAdEntity(Integer id);

    /**
     * Проверяет, является ли пользователь владельцем объявления.
     *
     * @param adId идентификатор объявления
     * @param username email пользователя
     * @return true если пользователь является владельцем, иначе false
     */
    boolean isAdOwner(Integer adId, String username);
}
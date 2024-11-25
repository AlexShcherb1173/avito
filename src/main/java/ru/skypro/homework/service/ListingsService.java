package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingsDTO;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * Интерфейс ListingsService предоставляет методы для управления объявлениями.
 * Он включает операции для получения, добавления, обновления и удаления объявлений,
 * а также для работы с изображениями, связанными с объявлениями.
 */
public interface ListingsService {

    /**
     * <b>Получает список всех объявлений. </b> <p>
     *
     * @return список объектов ListingDTO, представляющих все объявления
     */
    ListingsDTO getAllListings();

    /**
     * <b>Добавляет новое объявление. </b> <p>
     *
     * @param createOrUpdateListingDTO объект CreateOrUpdateListing, содержащий данные нового объявления
     * @param image файл изображения, связанного с объявлением
     * @param authentication объект Authentication, представляющий текущего пользователя
     * @return объект ListingsDTO, представляющий добавленное объявление
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    ListingsDTO addListing(CreateOrUpdateListing createOrUpdateListingDTO, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * <b>Получает подробную информацию об объявлении по его идентификатору. </b> <p>
     *
     * @param id идентификатор объявления
     * @return объект ExtendedListingDTO, представляющий запрашиваемое объявление
     */
    ExtendedListingDTO getListing(long id);

    /**
     * <b>Удаляет объявление по его идентификатору. </b> <p>
     *
     * @param id идентификатор объявления, которое нужно удалить
     * @param authentication объект Authentication, представляющий текущего пользователя
     * @throws AccessDeniedException если у пользователя нет прав на удаление объявления
     */
    void deleteListing(long id, Authentication authentication) throws AccessDeniedException;

    /**
     * <b>Обновляет существующее объявление. </b> <p>
     *
     * @param id идентификатор объявления, которое нужно обновить
     * @param createOrUpdateListing объект CreateOrUpdateListing с новыми данными
     * @param authentication объект Authentication, представляющий текущего пользователя
     * @return объект ListingsDTO, представляющий обновленное объявление
     * @throws AccessDeniedException если у пользователя нет прав на обновление объявления
     */
    ListingsDTO updateListing(long id, CreateOrUpdateListing createOrUpdateListing, Authentication authentication) throws AccessDeniedException;

    /**
     * <b>Получает список объявлений, принадлежащих текущему пользователю. </b> <p>
     *
     * @param authentication объект Authentication, представляющий текущего пользователя
     * @return объект ListingsDTO, представляющий объявления текущего пользователя
     */
    ListingsDTO getListingsMe(Authentication authentication);

    /**
     * <b>Обновляет изображение для указанного объявления. </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     *
     * @param id идентификатор объявления, для которого нужно обновить изображение
     * @param image файл нового изображения
     * @param authentication объект Authentication, представляющий текущего пользователя
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    @Transactional
    void updateListingImage(Long id, MultipartFile image, Authentication authentication) throws IOException;

}
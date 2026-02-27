package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdMapperManual;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 * <p>
 * Реализует бизнес-логику CRUD-операций над объявлениями, хранит данные в БД через репозитории,
 * выполняет проверки прав доступа (владелец объявления или администратор),
 * а также преобразует сущности в DTO и обратно с помощью маппера.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    //private final AdMapper adMapper;
    private final AdMapperManual adMapper;

    /**
     * Возвращает список всех объявлений.
     *
     * @return DTO {@link Ads}, содержащий количество и список объявлений
     */
    public Ads getAllAds() {
        List<Ad> results = adRepository.findAll()
                .stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    /**
     * Возвращает объявления текущего пользователя по email (берётся из аутентификации).
     *
     * @param email email пользователя (username в Basic Auth)
     * @return DTO {@link Ads} со списком объявлений пользователя
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден
     */
    public Ads getAdsByAuthorEmail(String email) {
        UserEntity author = getUserByEmailOrThrow(email);

        List<Ad> results = adRepository.findAllByAuthor_Id(author.getId())
                .stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    /**
     * Возвращает расширенную информацию об объявлении по id.
     *
     * @param adId идентификатор объявления
     * @return DTO {@link ExtendedAd}
     * @throws org.springframework.web.server.ResponseStatusException если объявление не найдено
     */
    public ExtendedAd getExtendedAd(Integer adId) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));
        return adMapper.toExtendedDto(ad);
    }

    /**
     * Создаёт новое объявление от имени пользователя (author определяется по email из аутентификации).
     *
     * @param authorEmail email автора объявления
     * @param createOrUpdateAd DTO с данными объявления
     * @param imagePath путь/имя файла изображения (в текущей реализации хранится строкой)
     * @return DTO {@link Ad} созданного объявления
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден
     */
    public Ad addAd(String authorEmail, CreateOrUpdateAd createOrUpdateAd, String imagePath) {
        UserEntity author = getUserByEmailOrThrow(authorEmail);

        AdEntity entity = new AdEntity();
        entity.setAuthor(author);
        entity.setImage(imagePath);

        //adMapper.applyCreateOrUpdate(createOrUpdateAd, entity);
        adMapper.applyCreateOrUpdate(entity, createOrUpdateAd);


        AdEntity saved = adRepository.save(entity);
        return adMapper.toAdDto(saved);
    }

    /**
     * Обновляет объявление по id, если пользователь имеет право (автор объявления или ADMIN).
     *
     * @param adId id объявления
     * @param currentEmail email текущего пользователя
     * @param createOrUpdateAd DTO с обновляемыми полями
     * @return DTO {@link Ad} после обновления
     * @throws org.springframework.web.server.ResponseStatusException если объявление не найдено или нет прав
     */
    public Ad updateAd(Integer adId, String currentEmail, CreateOrUpdateAd createOrUpdateAd) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adMapper.applyCreateOrUpdate(ad, createOrUpdateAd);
        AdEntity saved = adRepository.save(ad);
        return adMapper.toAdDto(saved);
    }

    /**
     * Удаляет объявление по id, если пользователь имеет право (автор объявления или ADMIN).
     *
     * @param adId id объявления
     * @param currentEmail email текущего пользователя
     * @throws org.springframework.web.server.ResponseStatusException если объявление не найдено или нет прав
     */
    public void deleteAd(Integer adId, String currentEmail) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adRepository.delete(ad);
    }

    /**
     * Обновляет изображение объявления, если пользователь имеет право.
     * <p>В текущей реализации сохраняется только строка imagePath.</p>
     *
     * @param adId id объявления
     * @param currentEmail email текущего пользователя
     * @param imagePath путь/имя файла изображения
     * @return массив байт изображения (в текущей реализации возвращается пустой массив)
     * @throws org.springframework.web.server.ResponseStatusException если объявление не найдено или нет прав
     */
    public byte[] updateAdImage(Integer adId, String currentEmail, String imagePath) {
        UserEntity currentUser = getUserByEmailOrThrow(currentEmail);

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));

        checkAdPermission(currentUser, ad);

        adRepository.save(ad);
        return new byte[0];
    }

    private UserEntity getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void checkAdPermission(UserEntity currentUser, AdEntity ad) {
        if (currentUser.getRole() == UserRole.ADMIN) {
            return;
        }
        Integer ownerId = ad.getAuthor() != null ? ad.getAuthor().getId() : null;
        if (ownerId == null || !ownerId.equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission for this ad");
        }
    }

    // AdService.java

    public byte[] getImageBytes(Integer adId) {
        // Находим объявление
        AdEntity ad = adRepository.findById(adId).orElseThrow(/*...*/);

        // Берем имя файла из БД (например, "151515.jpg")
        String fileName = ad.getImage();

        // Читаем файл с диска
        File file = new File("ПУТЬ_К_ПАПКЕ_С_КАРТИНКАМИ/" + fileName);

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл", e);
        }
    }

}

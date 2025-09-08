package ru.skypro.homework.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUtil;
import ru.skypro.homework.service.AdsService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 * Обеспечивает полный CRUD функционал, а также работу с изображениями и доступом по ролям.
 */
@Service
@Transactional
public class AdsServiceImpl implements AdsService {

    private final AdRepository ads;
    private final UserRepository users;
    private final AdMapper mapper;
    private final SecurityUtil sec;

    public AdsServiceImpl(AdRepository ads, UserRepository users, AdMapper mapper, SecurityUtil sec) {
        this.ads = ads;
        this.users = users;
        this.mapper = mapper;
        this.sec = sec;
    }

    /**
     * Получает список всех объявлений.
     *
     * @return DTO, содержащий общее количество и список объявлений
     */
    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        List<Ad> list = ads.findAll().stream().map(mapper::toAdDto).collect(Collectors.toList());
        Ads res = new Ads();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

    /**
     * Создаёт новое объявление.
     *
     * @param props DTO с текстом объявления
     * @param image изображение (не обрабатывается на этом этапе)
     * @return созданное объявление
     */
    @Override
    public Ad addAd(CreateOrUpdateAd props, MultipartFile image) {
        UserEntity me = sec.currentUser();
        AdEntity e = mapper.fromCreateDto(props);
        e.setAuthor(me);
        AdEntity saved = ads.save(e);
        return mapper.toAdDto(saved);
    }

    /**
     * Получает объявления, созданные текущим пользователем.
     *
     * @return DTO со списком своих объявлений
     */
    @Override
    @Transactional(readOnly = true)
    public Ads getMyAds() {
        UserEntity me = sec.currentUser();
        List<Ad> list = ads.findAllByAuthor_Id(me.getId()).stream().map(mapper::toAdDto).collect(Collectors.toList());
        Ads res = new Ads();
        res.setCount(list.size());
        res.setResults(list);
        return res;
    }

    /**
     * Получает подробную информацию об объявлении по ID.
     *
     * @param id идентификатор объявления
     * @return DTO с полной информацией об объявлении
     * @throws IllegalArgumentException если объявление не найдено
     */
    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAd(int id) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        return mapper.toExtendedDto(e);
    }

    /**
     * Удаляет объявление по ID, если пользователь — владелец или админ.
     *
     * @param id идентификатор объявления
     * @throws AccessDeniedException если доступ запрещён
     */
    @Override
    public void removeAd(int id) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        ads.delete(e);
    }

    /**
     * Обновляет объявление по ID.
     * Разрешено владельцу или админу.
     *
     * @param id  идентификатор объявления
     * @param dto DTO с новыми данными
     * @return обновлённое объявление
     * @throws AccessDeniedException если доступ запрещён
     */
    @Override
    public Ad updateAd(int id, CreateOrUpdateAd dto) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        mapper.updateEntity(dto, e);
        return mapper.toAdDto(e);
    }

    /**
     * Обновляет изображение объявления по ID.
     * Метод пока возвращает пустой массив (заглушка).
     *
     * @param id    идентификатор объявления
     * @param image изображение
     * @return пустой массив байтов
     * @throws AccessDeniedException если доступ запрещён
     */
    @Override
    public byte[] updateImage(int id, MultipartFile image) {
        AdEntity e = ads.findById(id).orElseThrow(() -> new IllegalArgumentException("ad not found"));
        UserEntity me = sec.currentUser();
        if (!sec.isOwner(me, e.getAuthor().getId()) && !sec.isAdmin(me)) {
            throw new AccessDeniedException("forbidden");
        }
        return new byte[0];
    }
}


package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 * Реализует бизнес-логику создания, получения,
 * обновления и удаления объявлений.
 */

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    /**
     * Возвращает список всех объявлений.
     *
     * @return объект Ads со списком объявлений
     */

    @Override
    public Ads getAllAds() {

        List<Ad> ads = adRepository.findAll();

        List<ru.skypro.homework.dto.Ad> dtoList =
                ads.stream()
                        .map(adMapper::toDto)
                        .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(dtoList.size());
        result.setResults(dtoList);

        return result;
    }

    /**
     * Возвращает объявления текущего пользователя.
     *
     * @return объект Ads со списком объявлений пользователя
     */

    @Override
    public Ads getAdsMe() {

        User currentUser = getCurrentUser();

        List<Ad> ads = adRepository.findByAuthorId(currentUser.getId());

        List<ru.skypro.homework.dto.Ad> dtoList =
                ads.stream()
                        .map(adMapper::toDto)
                        .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(dtoList.size());
        result.setResults(dtoList);

        return result;
    }

    /**
     * Создает новое объявление.
     *
     * @param properties данные объявления
     * @param image изображение объявления
     * @return созданное объявление
     */

    @Override
    public ru.skypro.homework.dto.Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {

        User author = getCurrentUser();

        Ad ad = adMapper.toEntity(properties);
        ad.setAuthor(author);

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = imageService.saveImage(image);
                ad.setImage("/images/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image", e);
            }
        }

        Ad saved = adRepository.save(ad);

        return adMapper.toDto(saved);
    }

    /**
     * Возвращает объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return расширенная информация об объявлении
     */

    @Override
    public ExtendedAd getAdById(Long id) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        return adMapper.toExtendedDto(ad);
    }

    /**
     * Удаляет объявление по идентификатору.
     *
     * @param id идентификатор объявления
     */

    @Override
    public void deleteAd(Long id) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(ad, currentUser)) {
            throw new AccessDeniedException("You cannot delete чужое объявление");
        }

        adRepository.delete(ad);
    }

    /**
     * Обновляет информацию объявления.
     *
     * @param id идентификатор объявления
     * @param properties новые данные объявления
     * @return обновленное объявление
     */

    @Override
    public ru.skypro.homework.dto.Ad updateAd(Long id, CreateOrUpdateAd properties) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(ad, currentUser)) {
            throw new AccessDeniedException("You cannot edit чужое объявление");
        }

        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());

        Ad updated = adRepository.save(ad);

        return adMapper.toDto(updated);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id идентификатор объявления
     * @param image новый файл изображения
     * @return байты изображения
     */

    @Override
    public byte[] updateImage(Long id, MultipartFile image) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(ad, currentUser)) {
            throw new AccessDeniedException("You cannot update image чужого объявления");
        }

        if (image != null && !image.isEmpty()) {
            try {

                String fileName = imageService.saveImage(image);

                ad.setImage("/images/" + fileName);

                adRepository.save(ad);

                return imageService.getImage(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Cannot save image", e);
            }
        }

        return new byte[0];
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isOwnerOrAdmin(Ad ad, User user) {
        return ad.getAuthor().getId().equals(user.getId())
                || user.getRole().name().equals("ADMIN");
    }
}
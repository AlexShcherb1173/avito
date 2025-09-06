package ru.skypro.homework.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import java.time.LocalDateTime;
import java.util.List;

// Реализация сервиса для работы с объявлениями.
// Обеспечивает создание, получение, обновление
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);

    // Создание объявления из multipart данных.
    // @param userDetails данные аутентификации пользователя
    // @param createOrUpdateAd DTO с данными объявления
    // @param image файл изображения
    // @return AdDto созданного объявления
    @Override
    public AdDto createAdFromMultipart(UserDetails userDetails, CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        if (userDetails == null) {
            log.warn("Попытка создания объявления без аутентификации");
            throw new AccessDeniedException("User not authenticated");
        }

        log.info("Начало создания объявления для пользователя: {}", userDetails.getUsername());

        if (image.isEmpty()) {
            log.warn("Получен пустой файл изображения");
            throw new IllegalArgumentException("Image file is empty");
        }

        User dbUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("Пользователь не найден в БД: {}", userDetails.getUsername());
                    return new EntityNotFoundException("User not found");
                });

        Ad ad = AdMapper.INSTANCE.toAd(createOrUpdateAd);

        ad.setAuthor(dbUser);
        ad.setCreatedAt(LocalDateTime.now());

        try {
            String filename = imageService.saveImage(image, "ads");
            ad.setImage("/images/ads/" + filename);
        } catch (Exception e) {
            log.error("Ошибка при сохранении изображения", e);
            throw new RuntimeException("Failed to save image", e);
        }

        Ad saved = adRepository.save(ad);

        AdDto result = AdMapper.INSTANCE.toAdDto(saved);

        log.info("Объявление успешно создано, ID: {}", result.getPk());
        return result;
    }

    // Получение расширенной информации об объявлении.
    // @param id ID объявления
    // @return ExtendedAdDto с полной информацией

    @Override
    public ExtendedAdDto getExtendedAd(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        return adMapper.toExtendedAdDto(ad); // ✅ Преобразуем Ad → ExtendedAdDto
    }

    // Обновление объявления.
    // @param id ID объявления
    // @param dto DTO с данными для обновления
    // @return AdDto обновленного объявления

    @Override
    public AdDto updateAd(Long id, CreateOrUpdateAd dto) {
        log.info("Обновление объявления ID: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Объявление не найдено для обновления: ID={}", id);
                    return new EntityNotFoundException("Ad not found with id: " + id);
                });

        // Обновляем только те поля, которые пришли в DTO
        if (dto.getTitle() != null) {
            ad.setTitle(dto.getTitle());
        }
        if (dto.getPrice() != null) {
            ad.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            ad.setDescription(dto.getDescription());
        }

        Ad updatedAd = adRepository.save(ad);
        log.info("Объявление успешно обновлено: ID={}", id);

        return AdMapper.INSTANCE.toAdDto(updatedAd);
    }

    // Удаление объявления.
    // @param id ID объявления

    @Override
    public void deleteAd(Long id) {
        log.info("Удаление объявления ID: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Объявление не найдено для удаления: ID={}", id);
                    return new EntityNotFoundException("Ad not found with id: " + id);
                });

        adRepository.delete(ad);
        log.info("Объявление успешно удалено: ID={}", id);
    }

    // Обновление изображения объявления.
    // @param id ID объявления
    // @param image файл изображения
    // @return String URL нового изображения

    @Override
    public String updateAdImage(Long id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        String filename = imageService.saveImage(image, "ads");
        String imageUrl = "/images/ads/" + filename;
        ad.setImage(imageUrl);
        adRepository.save(ad);

        return imageUrl;
    }

    // Получение объявлений текущего пользователя.
    // @param username имя пользователя
    // @return AdsResponse с объявлениями пользователя

    @Override
    public AdsResponse getMyAds(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdDto> dtos = AdMapper.INSTANCE.toAdDtoList(ads);
        return new AdsResponse(dtos.size(), dtos);
    }

    // Получение всех объявлений.
    // @return AdsResponse со всеми объявлениями

    @Override
    public AdsResponse getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDto> dtos = adMapper.toAdDtoList(ads); // ✅ Преобразуем список Ad → AdDto
        return new AdsResponse(dtos.size(), dtos);
    }

    // Проверка, является ли пользователь владельцем объявления.
    // @param adId ID объявления
    // @param username имя пользователя
    // @return true если пользователь является владельцем

    @Override
    public boolean isOwner(Long adId, String username) {
        return adRepository.findById(adId)
                .map(ad -> {
                    boolean isOwner = ad.getAuthor().getUsername().equals(username);
                    log.debug("User {} is owner of ad {}: {}", username, adId, isOwner);
                    return isOwner;
                })
                .orElse(false);
    }
}
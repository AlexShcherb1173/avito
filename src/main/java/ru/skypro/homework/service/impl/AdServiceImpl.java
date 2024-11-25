package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Slf4j
public class AdServiceImpl implements AdService {

    @Autowired
    private final AdRepository adRepository;
    private final ImageServiceImpl imageService;

    public AdServiceImpl(AdRepository adRepository, ImageServiceImpl imageService) {
        this.adRepository = adRepository;
        this.imageService = imageService;
    }

    public void getAllAds() {
        adRepository.findAll();
    }

    public void removeAd(Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        adRepository.delete(ad);
    }

    public void addAd(CreateOrUpdateAd adProperties, MultipartFile image, String username) throws IOException {
        AdEntity ad = new AdEntity();
        ad.setTitle(adProperties.getTitle());
        ad.setPrice(adProperties.getPrice());
        ad.setDescription(adProperties.getDescription());
        try {
            String imageUrl = imageService.saveImageToDisk(image, username);// Сохраняем изменения в бд
            ad.setImage(imageUrl); // Обновляем URL изображения
        } catch (IOException e) {
            log.error("Ошибка при загрузке в методе addAd изображения: {}", e.getMessage());
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
        adRepository.save(ad);
    }

    public AdEntity getAds(Integer id) {
        if (!adRepository.existsById(id)) {
            throw new EntityNotFoundException("Объявление с " + id + " не найдено.");
        }
        return adRepository.findById(id).orElse(null);
    }

    public void updateAd(Integer id, AdEntity adModel) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление с " + id + " не найдено."));
        if (adModel.getTitle() != null) {
            ad.setTitle(adModel.getTitle());
        }
        if (adModel.getDescription() != null) {
            ad.setDescription(adModel.getDescription());
        }
        if (adModel.getPrice() != null) {
            ad.setPrice(adModel.getPrice());
        }
        adRepository.save(ad);
    }

    public void getAdsMe(Integer id) {
        if (!adRepository.existsById(id)) {
            throw new EntityNotFoundException("Объявление с " + id + " не найдено.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        AdEntity ad = adRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Владелец объявления с " + id + " не найден"));

        if (!isOwner(id, currentUsername)) {
            throw new AccessDeniedException("У вас нет доступа к этому объявлению");
        }
    }

    public void updateImage(Integer id, MultipartFile imageUpdate, String username) {
        AdEntity ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление не найдено"));
        ImageEntity imageModel = ad.getImageModel();
        if (imageUpdate != null && !imageUpdate.isEmpty()) { // Проверяем, что файл не пустой
            log.error("Изображение не может быть пустым");
            throw new IllegalArgumentException("Изображение не может быть пустым");
        }
        try {
            String mediaType = imageUpdate.getContentType(); // Получаем тип медиафайла
            if (mediaType == null || !mediaType.startsWith("image/")) {
                log.error("Файл должен быть изображением, получен тип: {}", mediaType);
                throw new IllegalArgumentException("Файл должен быть изображением");
            }

            String imageUrl = imageService.saveImageToDisk(imageUpdate, username);
            long fileSize = imageUpdate.getSize();

            ad.setImage(imageUrl); // Обновляем URL изображения
            imageModel.setFilePath(imageUrl); // Обновляем путь к файлу
            imageModel.setFileSize(fileSize); // Устанавливаем размер файла
            imageModel.setMediaType(mediaType); // Устанавливаем тип файла

            adRepository.save(ad); // Сохраняем изменения в бд
            log.info("Изображение для объявления успешно обновлено с ID: {}", id);
        } catch (IOException e) {
            log.error("Ошибка при загрузке в методе updateImage изображения: {}", e.getMessage());
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
    }

    //Метод проверки наличия объявления
    public boolean existsById(Integer id) {
        return !adRepository.existsById(id);
    }

    //Метод для проверки, является ли пользователь владельцем объявления
    public boolean isOwner(Integer id, String username) {
        AdEntity ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление с " + id + " не найдено"));
        return ad.getOwner().getUsername().equals(username);
    }
}

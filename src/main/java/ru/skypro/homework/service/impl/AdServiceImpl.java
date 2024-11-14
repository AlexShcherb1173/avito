package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdServiceImpl implements AdService {

    @Autowired
    private final AdRepository adRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public AdServiceImpl(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public List<AdModel> getAllAds() {
        return adRepository.findAll();
    }

    public void removeAd(Integer id) {
        AdModel ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        adRepository.delete(ad);
    }

    public void addAd(CreateOrUpdateAd adProperties, MultipartFile image) throws IOException {
        AdModel ad = new AdModel();
        ad.setTitle(adProperties.getTitle());
        ad.setPrice(adProperties.getPrice());
        ad.setDescription(adProperties.getDescription());
        try {
            String imageUrl = saveImage(image);
            ad.setImage(imageUrl); // Обновляем URL изображения
            adRepository.save(ad); // Сохраняем изменения в бд
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
        adRepository.save(ad);
    }

    public AdModel getAds(Integer id) {
        if (!adRepository.existsById(id)) {
            throw new EntityNotFoundException("Объявление с " + id + " не найдено.");
        }
        return adRepository.findById(id).orElse(null);
    }

    public void updateAd(Integer id, AdModel adModel) {
        AdModel ad = adRepository.findById(id)
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

    public AdModel getAdsMe(Integer id) {
        if (!adRepository.existsById(id)) {
            throw new EntityNotFoundException("Объявление с " + id + " не найдено.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        AdModel ad = adRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Владелец объявления с " + id + " не найден"));

        if (!isOwner(id, currentUsername)) {
            throw new AccessDeniedException("У вас нет доступа к этому объявлению");
        }
        return ad;
    }

    public void updateImage(Integer id, MultipartFile imageUpdate) {
        AdModel ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        if (imageUpdate != null && !imageUpdate.isEmpty()) { // Проверяем, что файл не пустой
            try {
                String imageUrl = saveImage(imageUpdate);
                ad.setImage(imageUrl); // Обновляем URL изображения
                adRepository.save(ad); // Сохраняем изменения в бд
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при загрузке изображения", e);
            }
        } else {
            throw new IllegalArgumentException("Изображение не может быть пустым");
        }
    }

    // Логика для сохранения изображения
    private String saveImage(MultipartFile imageUpdate) throws IOException {
        String originalFilename = imageUpdate.getOriginalFilename(); // Создаем уникальное имя для изображения
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        Path filePath = Paths.get(uploadDir, uniqueFileName); // Определяем путь к файлу

        Files.createDirectories(filePath.getParent()); // Создаем директорию, если она не существует

        Files.copy(imageUpdate.getInputStream(), filePath); // Сохраняем файл

        return "/images/" + uniqueFileName; // Возвращаем URL для доступа к изображению
    }

    //Метод проверки наличия объявления
    public boolean existsById(Integer id) {
        return !adRepository.existsById(id);
    }

    //Метод для проверки, является ли пользователь владельцем объявления
    public boolean isOwner(Integer id, String username) {
        AdModel ad = adRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление с " + id + " не найдено"));
        return ad.getOwner().getUsername().equals(username);
    }
}

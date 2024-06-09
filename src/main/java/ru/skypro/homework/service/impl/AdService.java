package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdServiceApi;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdService implements AdServiceApi {

    private final AdRepository adRepository;

    @Override
    public List<Ad> getAllAds() {
        log.info("Получение всех объявлений");
        return adRepository.findAll();
    }

    @Override
    @Transactional
    public Ad createAdWithImage(Ad ad, Image image) {
        log.info("Сохранение объявления: {}", ad.getTitle() + ", " + ad.getPrice() + ", " + ", " + ad.getDescription());
        ad.setImage(image);
        return adRepository.save(ad);
    }

    @Override
    public Ad getAdById(Long id) {
        log.info("Поиск объявления по номеру: {}", id);
        return adRepository.findById(id).orElseThrow(() -> {
            log.error("Объявление номером: {}", id + " не найдено");
            return new AdNotFoundException("Такого объявления не найдено");
        });
    }

    @Override
    public void deleteAd(Long id) {
        if (adRepository.findById(id).isEmpty()) {
            log.error("Объявление с номером: {}", id + " не найдено");
            throw new AdNotFoundException("Такого объявления не найдено");
        }
        log.info("Удаление объявления по номеру: {}", id);
        adRepository.deleteById(id);
    }

    @Override
    public Ad updateAdDetails(Long id, CreateOrUpdateAd properties) {
        log.info("Обновление объявления: {}", id);
        Ad ad = getAdById(id);
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        return adRepository.save(ad);
    }

    @Override
    public List<Ad> getUserAds(User user) {
        log.info("Поиск всех объявлений: {}", user.getUsername());
        return adRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Ad updateAdImage(Long id, Image image) {
        log.info("Обновление объявления по номеру: {}", id);
        Ad ad = getAdById(id);
        ad.setImage(image);
        return adRepository.save(ad);
    }

    @Override
    public Ad save(Ad ad) {
        log.info("Сохранение объявления: {}", ad.getTitle() + ", " + ad.getPrice() + ", " + ad.getDescription());
        return adRepository.save(ad);
    }

}

package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream().map(adMapper::toDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd properties, byte[] image) {
        AdEntity entity = adMapper.toEntity(properties);
        // TODO: установить автора из текущего пользователя
        // TODO: сохранить изображение
        AdEntity savedEntity = adRepository.save(entity);
        return adMapper.toDto(savedEntity);
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity entity = adRepository.findById(id).orElseThrow(() -> new RuntimeException("Ad not found"));
        return adMapper.toExtendedDto(entity);
    }

    @Override
    public void removeAd(Integer id) {
        adRepository.deleteById(id);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd) {
        AdEntity entity = adRepository.findById(id).orElseThrow(() -> new RuntimeException("Ad not found"));
        // TODO: обновить поля
        AdEntity savedEntity = adRepository.save(entity);
        return adMapper.toDto(savedEntity);
    }

    @Override
    public Ads getAdsMe() {
        // TODO: получить ID текущего пользователя
        Integer currentUserId = 1;
        List<AdEntity> entities = adRepository.findByAuthorId(currentUserId);
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream().map(adMapper::toDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    public void updateAdImage(Integer id, byte[] image) {
        // TODO: реализовать обновление изображения
    }
}

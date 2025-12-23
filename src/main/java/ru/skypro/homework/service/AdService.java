package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

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

    public Ads getAdsByAuthor(Integer authorId) {
        List<Ad> results = adRepository.findAllByAuthor_Id(authorId)
                .stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());

        Ads ads = new Ads();
        ads.setCount(results.size());
        ads.setResults(results);
        return ads;
    }

    public ExtendedAd getExtendedAd(Integer adId) {
        return adRepository.findById(adId)
                .map(adMapper::toExtendedDto)
                .orElse(null);
    }

    public Ad addAd(Integer authorId, CreateOrUpdateAd createOrUpdateAd, String imagePath) {
        UserEntity author = userRepository.findById(authorId).orElseThrow();

        AdEntity entity = new AdEntity();
        entity.setAuthor(author);
        entity.setImage(imagePath);

        adMapper.applyCreateOrUpdate(createOrUpdateAd, entity);

        AdEntity saved = adRepository.save(entity);
        return adMapper.toAdDto(saved);
    }

    public Ad updateAd(Integer adId, CreateOrUpdateAd createOrUpdateAd) {
        return adRepository.findById(adId)
                .map(entity -> {
                    // ВАЖНО: порядок аргументов для MapStruct
                    adMapper.applyCreateOrUpdate(createOrUpdateAd, entity);
                    return adMapper.toAdDto(entity);
                })
                .orElse(null);
    }

    public void deleteAd(Integer adId) {
        adRepository.deleteById(adId);
    }
}

package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;


    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;

    }

    @Override
    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream().map(AdMapper.INSTANCE::toDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    @Transactional
    public Ad addAd(CreateOrUpdateAd properties, byte[] image, Authentication authentication) {
        Users author = userRepository.findByEmail(authentication.name())
                .orElseThrow(() -> new RuntimeException(" Пользователь не найден "));
        AdEntity entity = AdMapper.INSTANCE.toEntity(properties);
        entity.setAuthor(author);
        AdEntity savedEntity = adRepository.save(entity);
        return AdMapper.INSTANCE.toDto(savedEntity);
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity entity = adRepository.findById(id).orElseThrow(() -> new RuntimeException(" Объявление не найдено "));
        return AdMapper.INSTANCE.toExtendedDto(entity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#id, authentication.name)")
    @Transactional
    public void removeAd(Integer id) {
        adRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#id, authentication.name)")
    @Transactional
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd) {
        AdEntity entity = adRepository.findById(id).orElseThrow(() -> new RuntimeException(" Объявление не найдено "));
        if (updateAd.getTitle() != null) entity.setTitle(updateAd.getTitle());
        if (updateAd.getPrice() != null) entity.setPrice(updateAd.getPrice());
        if (updateAd.getDescription() != null) entity.setDescription(updateAd.getDescription());

        AdEntity savedEntity = adRepository.save(entity);
        return AdMapper.INSTANCE.toDto(savedEntity);
    }

    @Override
    public Ads getAdsMe(Authentication authentication) {
        Users user = userRepository.findByEmail(authentication.name()).orElseThrow(() -> new RuntimeException("User not found"));

        List<AdEntity> entities = adRepository.findByAuthorId(user.getId());
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream().map(AdMapper.INSTANCE::toDto).collect(Collectors.toList()));
        return ads;
    }


    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#adId, authentication.name)")
    public Ads getAdsByUser(Integer userId) {
        List<AdEntity> entities = adRepository.findByAuthorId(userId);
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream()
                .map(AdMapper.INSTANCE::toDto)
                .collect(Collectors.toList()));
        return ads;
    }

    public String getAdAuthorEmail(Integer adId) {
        return adRepository.findById(adId)
                .map(ad -> ad.getAuthor().getEmail())
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

}


package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CreateOrUpdateAdMapper;
import ru.skypro.homework.mapper.ExtendedAdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public AdServiceImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    @Override
    public Ad add(AdEntity adEntity) {
        AdEntity savedAd = adRepository.save(adEntity);
        return AdMapper.INSTANCE.toDTO(savedAd);
    }

    @Override
    public ExtendedAd getExtendedAd(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow();
        AdEntity adEntity = adRepository.findById(id).orElseThrow();
        return ExtendedAdMapper.INSTANCE.toExtendedAd(userEntity, adEntity);
    }

    @Override
    public Ad update(Long id, CreateOrUpdateAd updatedAd) {

        AdEntity adEntity = adRepository.findById(id).orElseThrow();
        AdEntity adEntityUpdated = CreateOrUpdateAdMapper.INSTANCE.toEntity(updatedAd);

        adEntity.setTitle(adEntityUpdated.getTitle());
        adEntity.setPrice(adEntityUpdated.getPrice());
        adEntity.setDescription(adEntityUpdated.getDescription());

        AdEntity savedAd = adRepository.save(adEntity);
        return AdMapper.INSTANCE.toDTO(savedAd);
    }

    @Override
    public void delete(Long id) {
        adRepository.deleteById(id);
    }

    @Override
    public Ads getAds() {

        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> results = adEntities.stream()
                .map(AdMapper.INSTANCE::toDTO)
                .toList();

        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(results);
        return ads;
    }

    @Override
    public Ad createAdWithImage(CreateOrUpdateAd updatedAd, MultipartFile adFile) {

        AdEntity adEntityCreated = CreateOrUpdateAdMapper.INSTANCE.toEntity(updatedAd);

//                                                                      ? Запись image в бд

        AdEntity savedAd = adRepository.save(adEntityCreated);
        return AdMapper.INSTANCE.toDTO(savedAd);
    }

    @Override
    public Ads getAdsMe() {

        List<AdEntity> adEntities = adRepository.findAll();

        List<Ad> results = adEntities.stream()
//                .filter()                                                 ?
                .map(AdMapper.INSTANCE::toDTO)
                .toList();

        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(results);
        return ads;
    }

}

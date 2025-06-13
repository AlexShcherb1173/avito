package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AppMapper appMapper;

    public AdServiceImpl(AdRepository adRepository, AppMapper appMapper) {
        this.adRepository = adRepository;
        this.appMapper = appMapper;
    }


    @Override
    public Ads getAllAds() {
        return new Ads();
    }

    @Override
    public Ad addAd(Ad createOrUpdateAd) {
        AdEntity adEntity = appMapper.createOrUpdateToAdEntity(createOrUpdateAd);
        Ad ad = appMapper.adEntitytoAd(adEntity);
        adRepository.save(adEntity);
        return ad;
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return appMapper.adEntitytoExtendedAd(adEntity);
    }

    @Override
    public boolean deleteAd(Long id) {
        adRepository.deleteById(Math.toIntExact(id));
        return false;
    }

    @Override
    public Ad updateAd(Long id, Ad createOrUpdateAd) {
        return new Ad();
    }

    @Override
    public boolean updateAdImage(Long id, MultipartFile image) {

        return false;
    }

    @Override
    public ExtendedAd getAdById(Long id) {
        AdEntity adEntity = adRepository.findById(Math.toIntExact(id)).orElseThrow(RuntimeException::new);
        return appMapper.adEntitytoExtendedAd(adEntity);
    }

    @Override
    public List<Ad> getMyAds() {
        return List.of();
    }
}
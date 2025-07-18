package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.mapper.AppMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository repository;
    private final AppMapper mapper;

    @Override
    public Ads getAllAds() {
        return (Ads) repository.findAll().stream()
                .map(mapper::adEntityToAdDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile file) {
        Ad entity = mapper.createOrUpdateAdToAdEntity(createAd);
        return mapper.adEntityToAdDTO(repository.save(entity));
    }

    @Override
    public void removeAd(int id) {
        repository.deleteById(id);
    }

    @Override
    public AdDTO updateAds(int id, CreateOrUpdateAd updateAd) {
        Ad existingAd = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such ad found."));
        Ad updatedAd = mapper.updateAd(updateAd, existingAd);
        return mapper.adEntityToAdDTO(repository.save(updatedAd));
    }

    @Override
    public Ads getAdsMe() {
        return (Ads) repository.findAll().stream();
    }

    @Override
    public String updateImage(int id, MultipartFile image) {
        return "";
    }

    @Override
    public AdDTO getAds(int id) {
        return mapper.adEntityToAdDTO(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such ad found.")));
    }
}
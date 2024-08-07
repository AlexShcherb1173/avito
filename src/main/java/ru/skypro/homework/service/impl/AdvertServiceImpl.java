package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.Advert;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.mapper.AdvertMapper;
import ru.skypro.homework.repositories.AdvertRepository;
import ru.skypro.homework.service.AdvertService;

import java.util.Optional;

@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertRepository repository;

    @Autowired
    private AdvertMapper mapper;

    @Override
    public AdvertDto createAdvert(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile file) {
//        Photo photo =
//        Advert advert = mapper.createAdsDtoToAd(createOrUpdateAdDto);
//        return mapper.advertToAdsDto(repository.save(advert));
        return null;
    }

    @Override
    public AdvertDto getAdvertById(Long id) {
        Optional<Advert> advert = repository.findById(id);
        return advert.map(mapper::advertToAdsDto).orElse(null);
    }
}

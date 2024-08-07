package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.AuthenticationComponent;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.Advert;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.mapper.AdvertMapper;
import ru.skypro.homework.repositories.AdvertRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdvertService;
import ru.skypro.homework.service.ImageService;

import java.util.Optional;

@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertMapper mapper;

    @Autowired
    private ImageService service;

    @Autowired
    private AuthenticationComponent authentication;

    @Override
    public AdvertDto create(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile file) {
        Photo photo = service.uploadPhoto(file);
        Advert advert = mapper.createAdsDtoToAd(createOrUpdateAdDto);
        advert.setAuthor(userRepository.findByEmail(authentication.getAuth().getName()));
        advert.setPhoto(photo);
        return mapper.advertToAdsDto(advertRepository.save(advert));
    }

    @Override
    public void delete(long id) {
        Advert advert = findAdvertWithAuth(id);
        Photo photo = advert.getPhoto();
        advertRepository.delete(advert);
        service.deleteFile(photo);
    }

    @Override
    public AdvertDto update(long id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Advert advert = findAdvertWithAuth(id);
        mapper.updateAd(createOrUpdateAdDto, advert);
        advertRepository.save(advert);
        return mapper.advertToAdsDto(advert);
    }

    @Override
    public AdvertDto getAdvertById(long id) {
        Optional<Advert> advert = advertRepository.findById(id);
        return advert.map(mapper::advertToAdsDto).orElse(null);
    }

    private Advert findAdvertWithAuth(long id) {
        Optional<Advert> advert = advertRepository.findById(id);
        if (advert.isEmpty()) {
            throw new RuntimeException("Advert not found");
        }
        if (authentication.checkAuthNotEnough(advert.get().getAuthor().getEmail())) {
            throw new RuntimeException("Forbidden");
        }
        return advert.get();
    }
}

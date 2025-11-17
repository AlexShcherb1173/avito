package ru.skypro.homework.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final CollectionMapper collectionMapper;
    private ImageService imageService;
    private final FileStorageConfig fileStorageConfig;

    private static final String ADS_IMAGE_DIR = "ads";

    @Override
    public AdsDto getAllAds() {
        List<AdEntity> ads = adRepository.findAll();
        return collectionMapper.toAdsDto(ads);
    }

    @Override
    public ExtendedAdDto getAd(Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        return adMapper.toExtendedAdDto(ad);
    }

    @Override
    public AdsDto getMyAds(String username) {
        UserEntity user = getUserByUsername(username);
        List<AdEntity> userAds = adRepository.findByAuthorId(user.getId());
        return collectionMapper.toAdsDto(userAds);
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdDto createOrUpdateAdDto, String username, MultipartFile image) throws IOException {
        UserEntity author = getUserByUsername(username);

        AdEntity adEntity = adMapper.toEntity(createOrUpdateAdDto);
        adEntity.setAuthor(author);
        
        return null;
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String username) {
        return null;
    }

    @Override
    public void deleteAd(Integer id, String username) {

    }

    @Override
    public byte[] getAdImage(Integer id) throws IOException {
        return new byte[0];
    }

    @Override
    public String getAdImageImageContentType(Integer id) throws IOException {
        return "";
    }

    @Override
    public boolean updateAdImage(Integer id, MultipartFile image, String username) throws IOException {
        return false;
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found exception: " + username));
    }
}

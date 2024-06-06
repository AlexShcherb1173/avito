package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapperService;
import ru.skypro.homework.mapper.CreateOrUpdateService;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.utils.WorkWithFilesUtils;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {
    @Value(value = "${photo.dir.path}")
    private String photoDir;
    @Value(value = "${ads.image}")
    private String linkImage;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapperService adMapperService;
    private final CreateOrUpdateService createOrUpdateService;
    private final ImageRepository imageRepository;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository, AdMapperService adMapperService, CreateOrUpdateService createOrUpdateService, ImageRepository imageRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapperService = adMapperService;
        this.createOrUpdateService = createOrUpdateService;
        this.imageRepository = imageRepository;
    }

    @Override
    public AdsDto getAllAds() {
        List<AdDto> result = adRepository.findAll().stream()
                .map(adMapperService::mappingToDto).collect(Collectors.toList());
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(result.size());
        adsDto.setResults(result);
        return adsDto;
    }

    @Override
    public AdDto addAd(MultipartFile image, CreateOrUpdateAdDto properties) throws IOException {
        String username = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);
        if (userEntityOptional.isEmpty()) {
            return null;
        }
        UserEntity userEntity = userEntityOptional.get();
        AdEntity adEntity = createOrUpdateService.mappingToEntity(properties);
        adEntity.setUser(userEntity);
        adRepository.save(adEntity);
        ImageEntity imageEntity = WorkWithFilesUtils.loadImage(image, photoDir);
        imageRepository.save(imageEntity);
        adEntity.setImageEntity(imageEntity);
        adRepository.save(adEntity);
        return adMapperService.mappingToDto(adEntity);
    }

    @Override
    public ExtendedAdDto getAd(int id) {
        Optional<AdEntity> adEntityOptional = adRepository.findById(id);
        if (adEntityOptional.isEmpty()) {
            return null;
        }
        AdEntity adEntity = adEntityOptional.get();
        ExtendedAdDto result = new ExtendedAdDto();
        result.setPk(adEntity.getId());
        result.setAuthorFirstName(adEntity.getUser().getFirstName());
        result.setAuthorLastName(adEntity.getUser().getLastName());
        result.setDescription(adEntity.getDescription());
        result.setEmail(adEntity.getUser().getEmail());
        ImageEntity imageEntity = adEntity.getImageEntity();
        if (imageEntity != null) {
            result.setImage(String.format(linkImage, imageEntity.getId()));
        }
        result.setPhone(adEntity.getUser().getPhone());
        result.setPrice(adEntity.getPrice());
        result.setTitle(adEntity.getTitle());
        return result;
    }

    @Override
    public boolean removeAd(int id) throws IOException {
        Optional<AdEntity> adEntityOptional = adRepository.findById(id);
        if (adEntityOptional.isEmpty()) {
            return false;
        }
        AdEntity adEntity = adEntityOptional.get();
        adRepository.delete(adEntity);
        ImageEntity imageEntity = adEntity.getImageEntity();
        if (imageEntity != null) {
            imageRepository.delete(imageEntity);
            WorkWithFilesUtils.deleteFile(photoDir + "/" + imageEntity.getNameImage());
        }
        return true;
    }

    @Override
    public AdDto updateAds(int id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Optional<AdEntity> adEntityOptional = adRepository.findById(id);
        if (adEntityOptional.isEmpty()) {
            return null;
        }
        AdEntity adEntity = adEntityOptional.get();
        adEntity.setPrice(createOrUpdateAdDto.getPrice());
        adEntity.setTitle(createOrUpdateAdDto.getTitle());
        adEntity.setDescription(createOrUpdateAdDto.getDescription());
        adRepository.save(adEntity);
        return adMapperService.mappingToDto(adEntity);
    }

    @Override
    public AdsDto getAdsMe() {
        String username = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);
        if (userEntityOptional.isEmpty()) {
            return null;
        }
        List<AdEntity> ads = adRepository.findAllByUser(userEntityOptional.get());
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(ads.size());
        adsDto.setResults(ads.stream().map(adMapperService::mappingToDto).collect(Collectors.toList()));
        return adsDto;
    }

    @Override
    public byte[] updateImage(int id, MultipartFile image) throws IOException {
        Optional<AdEntity> adEntityOptional = adRepository.findById(id);
        if (adEntityOptional.isEmpty()) {
            return null;
        }
        ImageEntity imageEntity = adEntityOptional.get().getImageEntity();
        String oldName = imageEntity.getNameImage();
        WorkWithFilesUtils.deleteFile(photoDir + "/" + oldName);
        ImageEntity resImage = WorkWithFilesUtils.loadImage(image, photoDir);
        resImage.setId(imageEntity.getId());
        imageRepository.save(resImage);
        return WorkWithFilesUtils.getImage(resImage.getNameImage(), photoDir);
    }

    @Override
    public boolean hasAdAction(int adId) {
        String username = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);
        if (userEntityOptional.isEmpty()) {
            return false;
        }
        Optional<AdEntity> adEntityOptional = adRepository.findById(adId);
        if (adEntityOptional.isEmpty()) {
            return false;
        }
        AdEntity adEntity = adEntityOptional.get();
        UserEntity userEntity = userEntityOptional.get();
        return userEntity.equals(adEntity.getUser());
    }


    private String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}

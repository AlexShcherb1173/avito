package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.generatedDto.AdDto;
import ru.skypro.homework.dto.generatedDto.AdsDto;
import ru.skypro.homework.dto.generatedDto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.generatedDto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final UserService userService;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, ImageService imageService, UserService userService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
        this.userService = userService;
    }

    @Override
    public AdsDto getAllAds() {
        List<Ad> adList = adRepository.findAll();
        return adMapper.adListToAdsDto(adList.size(), adList);
    }

    @Override
    @Transactional
    public AdDto createAd(CreateOrUpdateAdDto createAdDto, MultipartFile image, Authentication authentication) {
        User currentUser = userService.getUser(authentication.getName());
        Ad ad = adMapper.createOrUpdateAdDtoToAd(createAdDto);
        ad.setAuthor(currentUser);
        Ad savedAd = adRepository.save(ad);
        Image adImage = imageService.createImage(image, savedAd);
        savedAd.setImage(adImage);
        return adMapper.adToAdDto(savedAd);
    }

    @Override
    public Ad getAdById(long id) {
        return adRepository.findById(id).orElseThrow(AdsNotFoundException::new);
    }

    @Override
    public void removeAd(long id, Authentication authentication) {
        Ad ad = getAdById(id);
        userService.checkUserHasPermit(authentication, ad.getAuthor().getUsername());
        adRepository.delete(ad);
    }

    @Override
    public ExtendedAdDto getFullAdById(long id) {
        Ad ad = getAdById(id);
        return adMapper.adToExtendedAdDto(ad);
    }

    @Override
    public AdDto updateAdById(long id, CreateOrUpdateAdDto createOrUpdateAdDto, Authentication authentication) {
        Ad oldAd = getAdById(id);
        userService.checkUserHasPermit(authentication, oldAd.getAuthor().getUsername());
        Ad infoToUpdate = adMapper.createOrUpdateAdDtoToAd(createOrUpdateAdDto);

        oldAd.setPrice(infoToUpdate.getPrice());
        oldAd.setTitle(infoToUpdate.getTitle());
        oldAd.setDescription(infoToUpdate.getDescription());

        Ad updatedAd = adRepository.save(oldAd);
        return adMapper.adToAdDto(updatedAd);
    }

    @Override
    public AdsDto getAllAdsForUser(String username) {
        List<Ad> userAdsList = adRepository.findAdByAuthorUsername(username);
        return adMapper.adListToAdsDto(userAdsList.size(), userAdsList);
    }
}

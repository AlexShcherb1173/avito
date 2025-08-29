package ru.skypro.homework.service.impl;


import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

import java.util.List;

@Log4j2
@Service
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final AdMapper adMapper;
    private final CommentServiceImpl commentService;

    public AdServiceImpl(AdRepository adRepository, ImageRepository imageRepository, UserRepository userRepository, UserServiceImpl userService, AdMapper adMapper,
                         CommentServiceImpl commentService) {
        this.adRepository = adRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.adMapper = adMapper;
        this.commentService = commentService;
    }

    @Override
    public AdsDto getAllAds() {
        AdsDto responseWrapperAds = new AdsDto();
        List<AdEntity> adList = adRepository.findAll();
        responseWrapperAds.setResults(adMapper.adListToAdsDtoList(adList));
        responseWrapperAds.setCount(adList.size());
        return responseWrapperAds;

    }

    @Override
    public AdDto addAd(CreateOrUpdateAdDto properties, MultipartFile file) throws IOException {
        AdEntity ad = adMapper.toAd(properties);
        ImageEntity image = new ImageEntity();
        image.setMediaType(file.getContentType());
        image.setData(file.getBytes());
        imageRepository.save(image);
        ad.setImage(image);
        ad.setUser(userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found" + file)));
        adRepository.save(ad);
        return adMapper.toAdsDto(ad);

    }

    @Override
    public ExtendedAdDto getAds(Integer id) {
        return adRepository.findById(id).map(adMapper::toFullAdsDto).orElse(null);
    }

    @Override
    public void removeAd(Integer id) {
        imageRepository.delete(adRepository.findById(id).map(AdEntity::getImage).orElseThrow());
        commentService.deleteCommentsByAdId(id);
        adRepository.deleteById(id);
    }

    @Override
    public AdsDto getAdsMe() {
        AdsDto responseWrapperAds = new AdsDto();
        UserEntity user = userRepository.findByUsername(userService.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<AdEntity> adList = adRepository.findAllByUserId(user.getId());
        responseWrapperAds.setResults(adMapper.adListToAdsDtoList(adList));
        responseWrapperAds.setCount(adList.size());
        return responseWrapperAds;
    }

    @Override
    public AdDto updateDto(Integer id, CreateOrUpdateAdDto properties) {
        AdEntity ad = adRepository.findById(id).orElseThrow();
        ad.setTitle(properties.getTitle());
        ad.setDescription(properties.getDescription());
        ad.setPrice(properties.getPrice());
        adRepository.save(ad);
        return adMapper.toAdsDto(ad);
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile file) throws IOException {
        AdEntity ad = adRepository.findById(id).orElseThrow();
        ImageEntity image = imageRepository.findById(ad.getId()).orElse(new ImageEntity());
        image.setMediaType(file.getContentType());
        image.setData(file.getBytes());
        imageRepository.save(image);
        ad.setImage(image);

    }

    @Override
    @Transactional(readOnly = true)
    public ImageEntity getAdImage(Integer adId) {
        return adRepository.findById(adId).map(AdEntity::getImage).orElse(null);
    }

    @Override
    //@PreAuthorize("hasRole(‘ROLE_VIEWER’)")
    public boolean hasAdAccess(Integer id) {
        AdEntity ad = adRepository.findById(id).orElseThrow();
        String currentUserRole = userService.getCurrentUserRole();
        String adCreatorUsername = ad.getUser().getUsername();
        String currentUsername = userService.getCurrentUsername();
        return currentUserRole.equals("ADMIN") || adCreatorUsername.equals(currentUsername);
    }

}

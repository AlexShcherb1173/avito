package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.AdNotFoundException;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final CurrentUserService currentUserService;
    private final ImageService imageService;

    @Override
    @Transactional
    public AdDto addAd(AdDto adDto) {
        User user = userRepository.findById(adDto.getAuthor())
                .orElseThrow(() -> new UserNotFoundException("User  not found"));

        Ad ad = new Ad();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setImage(adDto.getImage());
        ad.setUser (user);

        Ad savedAd = adRepository.save(ad);
        return adMapper.toDto(savedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public AdDto getAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Ad not found"));
        return adMapper.toDto(ad);
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAd adDto) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Ad not found"));
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());

        Ad updatedAd = adRepository.save(ad);
        return adMapper.toDto(updatedAd);
    }

    @PreAuthorize("hasRole('ADMIN') or @adSecurity.isAdOwner(#id, authentication.name)")
    @Override
    @Transactional
    public void deleteAd(Integer id) {
        if (!adRepository.existsById(id)) {
            throw new AdNotFoundException("Ad not found");
        }
        adRepository.deleteById(id);
    }

    @Override
    public Collection<AdDto> getAllAds() {
        Collection<Ad> ads = adRepository.findAll();
        if (ads.isEmpty()) {
            throw new AdNotFoundException("Ad not found");
        }
        return adRepository.findAll().stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdDto getAdById(Integer id) {
        return adMapper.toDto(adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Ad not found")));
    }

    @Override
    public Collection<AdDto> getAdsByUserId() {
        User user = currentUserService.getCurrentUser();
        return adRepository.findAdsByUserId(user.getId()).stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdDto updateImageAd(Integer id, MultipartFile file) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ad not found"));
        Image image = new Image();
        image.setImageUrl(file.getOriginalFilename());
        image.setAd(ad);

        try{
            image.setData(file.getBytes());
        } catch(IOException e){
            log.error("Ошибка чтения файла для пользователя {}: {}", ad.getPk(), e.getMessage());
            throw new RuntimeException("Ошибка при чтении файла. Попробуйте снова.", e);
        }
        ad.setImage(image.getImageUrl());
        return adMapper.toDto(adRepository.save(ad));
    }

}

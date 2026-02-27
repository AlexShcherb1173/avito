package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {

        List<Ad> ads = adRepository.findAll();

        List<ru.skypro.homework.dto.Ad> dtoList =
                ads.stream()
                        .map(adMapper::toDto)
                        .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(dtoList.size());
        result.setResults(dtoList);

        return result;
    }

    @Override
    public Ads getAdsMe() {

        User currentUser = userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No users in database"));

        List<Ad> ads = adRepository.findByAuthorId(currentUser.getId());

        List<ru.skypro.homework.dto.Ad> dtoList =
                ads.stream()
                        .map(adMapper::toDto)
                        .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(dtoList.size());
        result.setResults(dtoList);

        return result;
    }

    @Override
    public ru.skypro.homework.dto.Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {

        User author = userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No users in database"));

        Ad ad = adMapper.toEntity(properties);
        ad.setAuthor(author);

        if (image != null) {
            ad.setImage(image.getOriginalFilename());
        }

        Ad saved = adRepository.save(ad);

        return adMapper.toDto(saved);
    }

    @Override
    public ExtendedAd getAdById(Long id) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        return adMapper.toExtendedDto(ad);
    }

    @Override
    public void deleteAd(Long id) {
        adRepository.deleteById(id);
    }

    @Override
    public ru.skypro.homework.dto.Ad updateAd(Long id, CreateOrUpdateAd properties) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());

        Ad updated = adRepository.save(ad);

        return adMapper.toDto(updated);
    }

    @Override
    public byte[] updateImage(Long id, MultipartFile image) {

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (image != null) {
            ad.setImage(image.getOriginalFilename());
            adRepository.save(ad);

            try {
                return image.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Cannot read image");
            }
        }

        return new byte[0];
    }
}
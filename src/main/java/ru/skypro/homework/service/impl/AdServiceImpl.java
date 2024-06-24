package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final ImageService imageService;

    @Override
    public AdsDTO getAllAds() {
        List<AdDTO> ads = adRepository.findAll()
                .stream()
                .map(adMapper::modelToAdDTO)
                .collect(Collectors.toList());
        return new AdsDTO(ads.size(), ads);
    }

    @Override
    public AdDTO add(CreateOrUpdateAdDTO properties, MultipartFile image, String email) throws IOException {
        Ad ad = adMapper.createOrUpdateAdDTOToAd(properties);
        ad.setAuthor(userService.getUser(email));
        ad.setImage(imageService.uploadImage(image));
        ad.setAuthor(userService.getUser(email));
        return adMapper.modelToAdDTO(adRepository.save(ad));
    }

    @Override
    public ExtendedAdDTO getAds(int id) {
        log.info("getAds with {}. ", id);
        return adMapper.modelToExtendedAdDTO(adRepository.findById(id).orElseThrow(AdNotFoundException::new));
    }

    @Override
    public void removeAd(int id) {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        imageService.deleteImage(ad.getImage().getId());
        adRepository.deleteById(id);
    }

    @Override
    public AdDTO updateAds(int id, CreateOrUpdateAdDTO properties) {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        return adMapper.modelToAdDTO(adRepository.save(ad));
    }

    @Override
    public AdsDTO getAdsMe(String email) {
        int userId = userService.getUser(email).getId();
        List<AdDTO> myAds = adRepository.findAll()
                .stream()
                .filter(ad -> ad.getAuthor().getId() == userId)
                .map(adMapper::modelToAdDTO)
                .collect(Collectors.toList());
        return new AdsDTO(myAds.size(), myAds);
    }

    @Override
    public String[] updateImage(int id, MultipartFile image) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        ad.setImage(imageService.uploadImage(image));
        return new String[] {"/image/" + adRepository.save(ad).getImage().getId()};
    }
}

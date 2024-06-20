package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdServiceImpl implements AdService {

    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final ImageService imageService;
    private final UserRepository userRepository;


    @Override
    public AdsDTO getAllAds() {
        List<AdDTO> ads = adRepository.findAll()
                .stream()
                .map(adMapper::adToAdDTO)
                .collect(Collectors.toList());
        return new AdsDTO(ads.size(), ads);
    }

    @Override
    public AdDTO addAd(MultipartFile image, CreateOrUpdateAdDTO properties, String email) {
        try {

            Ad ad = adMapper.createOrUpdateAdToAd(properties, userService.getUser(email));
            ad.setImage(imageService.uploadImage(image));
            return adMapper.adToAdDTO(adRepository.save(ad));
        } catch (Exception e) {
            log.error("Ошибка при загрузке изображения: ");
        }
        /*
        Если возникает исключение метод возвращает
        null или может вернуть  объект
        AdDTO, (если Артур хочет) который указывает на ошибку.
         */
        return null;
    }


    @Override
    public ExtendedAdDTO getAds(int id) {
        return adMapper.adToExtendedAdDTO(adRepository.findById(id).orElseThrow(AdNotFoundException::new));
    }

    @Override
    public void removeAd(int id) {
        adRepository.deleteById(id);
    }

    @Override
    public AdDTO updateAds(int id, CreateOrUpdateAdDTO properties) {
        Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        return adMapper.adToAdDTO(adRepository.save(ad));
    }

    @Override
    public AdsDTO getAdsMe() {
        int userId = userService.getUser().getId();
        List<AdDTO> myAds = adRepository.findAll().stream()
                .filter(ad -> ad.getAuthor().getId() == userId)
                .map(adMapper::adToAdDTO).collect(Collectors.toList());
        return new AdsDTO(myAds.size(), myAds);
    }

    @Override
    public String[] updateImage(int id, MultipartFile image) {
        try {
            Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
            ad.setImage(imageService.uploadImage(image));
            return new String[]{"/image/" + adRepository.save(ad).getImage().getId()};
        } catch (IOException e) {
            log.error("Ошибка при загрузке изображения");
        }
        /* Возвращаем значение в случае ошибки
         если возникает исключение IOException, метод возвращает массив строк,
         который указывает на ошибку
         */
        return new String[]{"Ошибка при загрузке изображения"};
    }
}

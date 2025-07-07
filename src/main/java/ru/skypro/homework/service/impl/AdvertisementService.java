package ru.skypro.homework.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Advertisement.AdDTO;
import ru.skypro.homework.dto.Advertisement.Ads;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Advertisement.ExtendedAd;
import ru.skypro.homework.entity.Advertisement;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.AdMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdMapper adMapper;


    public Ads getAllAds() {
        List<Advertisement> ads = adRepository.findAll();
        List<AdDTO> adDTOs = ads.stream()
                .map(adMapper::toAdDTO)
                .collect(Collectors.toList());
        return new Ads(adDTOs.size(), adDTOs);
    }

    public AdDTO createAd(CreateOrUpdateAd properties, MultipartFile image, String email) throws IOException {
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        String imagePath = imageService.saveAdImage(image, null);

        Advertisement ad = Advertisement.builder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .price(properties.getPrice())
                .author(author)
                .image(imagePath)
                .build();

        Advertisement savedAd = adRepository.save(ad);
        return adMapper.toAdDTO(savedAd);
    }

    public ExtendedAd getExtendedAd(Long id) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return adMapper.toExtendedAd(ad);
    }

    public void deleteAd(Long id, String email) throws IOException {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        if (!ad.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("Вы не можете удалить чужие объявления");
        }

        adRepository.delete(ad);
        imageService.deleteImage(ad.getImage());
    }

    public Ads getAdsMe(String email) {
        List<AdDTO> ads = adRepository.findByAuthorEmail(email).stream()
                .map(adMapper::toAdDTO)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    public AdDTO updateAd(Long id, CreateOrUpdateAd updateDto, String email) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        if (!ad.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("Вы не можете редактировать чужие объявления");
        }

        ad.setTitle(updateDto.getTitle());
        ad.setDescription(updateDto.getDescription());
        ad.setPrice(updateDto.getPrice());

        Advertisement savedAd = adRepository.save(ad);
        return adMapper.toAdDTO(savedAd);
    }

    public String updateAdImage(Long adId, MultipartFile image, String username) throws IOException {
        UserEntity currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Advertisement ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        if (!ad.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете редактировать чужие объявления");
        }

        String oldImagePath = ad.getImage();
        String newImagePath = imageService.saveAdImage(image, adId);

        ad.setImage(newImagePath);
        adRepository.save(ad);

        if (oldImagePath != null) {
            imageService.deleteImage(oldImagePath);
        }

        return newImagePath;
    }



//
//    // Получение объявления по id
//    public List<Advertisement> getAllAds() {
//        return adRepository.findAll();
//    }
//
//
//    // Создание объявления
//    public Advertisement createAd (CreateOrUpdateAd properties, MultipartFile image, String email) {
//        UserEntity author = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//
//        String imagePath = imageService.saveAdImage(image, null);
//
//        Advertisement ad = Advertisement.builder()
//                .title(properties.getTitle())
//                .description(properties.getDescription())
//                .price(properties.getPrice())
//                .author(author)
//                .image(imagePath)
//                .build();
//
//        return adRepository.save(ad);
//    }
//
//    // Получение объявления по id
//    public Advertisement getAdById(Long id) {
//        return adRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
//    }
//
//    // Удаление объявления
//    public void deleteAd(Long id, String email) {
//        Advertisement ad = getAdById(id);
//
//        if (!ad.getAuthor().getEmail().equals(email)) {
//            throw new AccessDeniedException("вЫ не можете удалить чужие объявления");
//        }
//
//        adRepository.delete(ad);
//    }
//
//    public List<Advertisement> getUserAds(String email) {
//        return adRepository.findByAuthorEmail(email);
//    }
//
//    public Advertisement updateAd(Long id, CreateOrUpdateAd updateDto, String email) {
//        Advertisement ad = getAdById(id);
//
//        if (!ad.getAuthor().getEmail().equals(email)) {
//            throw new AccessDeniedException("You are not the owner of this ad");
//        }
//
//        ad.setTitle(updateDto.getTitle());
//        ad.setDescription(updateDto.getDescription());
//        ad.setPrice(updateDto.getPrice());
//
//        return adRepository.save(ad);
//    }
//
//
//    // Проверка, является ли пользователь владельцем объявления
//    public boolean isAdOwner(Long adId, String email) {
//        return adRepository.findById(adId)
//                .map(ad -> ad.getAuthor().getEmail().equals(email))
//                .orElse(false);
//    }
//
//    @Transactional
//    public String updateAdImage(Long adId, MultipartFile image, String username) {
//        UserEntity currentUser = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//        Advertisement ad = adRepository.findById(adId)
//                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
//
//        if (!ad.getAuthor().getId().equals(currentUser.getId())) {
//            throw new AccessDeniedException("Вы не можете редактировать чужие объявления");
//        }
//
//        String imagePath = imageService.saveAdImage(image, adId);
//        ad.setImage(imagePath);
//        adRepository.save(ad);
//
//        return imagePath;
//    }
//
//
//    public void updateAdImagePath(Long adId, String image) {
//        Advertisement ad = adRepository.findById(adId)
//                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
//        ad.setImage(image);
//        adRepository.save(ad);
//    }

}
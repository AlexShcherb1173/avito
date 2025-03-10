package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Component
public class AdMapper {

    private final UserRepository userRepository;
    private final ImageService imageService;


    public AdMapper(UserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    /**
     * Преобразует сущность Ad в DTO.
     *
     * @param ad Сущность Ad.
     * @return Преобразованный объект AdDto.
     */
    public AdDto adToDto(Ad ad) {
        if (ad == null) {
            throw new IllegalArgumentException("Объявление не может быть null");
        }

        AdDto adDto = new AdDto();
        adDto.setId(ad.getPk());
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        adDto.setImage("/image/" + ad.getImage().getId());
        adDto.setAuthor(ad.getUser() != null ? ad.getUser().getId() : null); // Убедимся, что автор не null
        return adDto;
    }

    /**
     * Преобразует DTO в сущность Ad.
     *
     * @param adDto DTO объявления.
     * @return Преобразованная сущность Ad.
     * @throws IllegalArgumentException Если DTO или автор не найдены.
     */
    public Ad dtoToAd(CreateOrUpdateAdDto ad, MultipartFile image, User user) throws IOException {
//        if (adDto == null) {
//            throw new IllegalArgumentException("DTO объявления не может быть null");
//        }
        Ad newAd = new Ad();
        newAd.setTitle(ad.getTitle());
        newAd.setPrice(ad.getPrice());
        newAd.setDescription(ad.getDescription());
        newAd.setImage(imageService.uploadImage(image));
        newAd.setUser(user);

//        Ad ad = new Ad();
//        ad.setPk(adDto.getId());
//        ad.setTitle(adDto.getTitle());
//        ad.setPrice(adDto.getPrice());
//        ad.setImage(adDto.getImage());
        // Находим пользователя по ID автора
//        User user = userRepository.findById(adDto.getAuthor())
//                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + adDto.getAuthor() + " не найден"));
//        ad.setUser(user);
        return newAd;
    }

    public Ads.AdDto mapToAdDto(Ad ad) {
        Ads.AdDto adDto = new Ads.AdDto();
        adDto.setPk(ad.getPk());
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        adDto.setImage("/image/" + ad.getImage().getId());
        adDto.setAuthor(ad.getUser().getId());
        return adDto;
    }

    public ExtendedAdDto mapToExtendedAdDto(Ad ad, User user) {
        if (ad == null) {
            throw new IllegalArgumentException("Объявление не может быть пу");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь в объявлении не может быть null");
        }
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();
        extendedAdDto.setPk(ad.getPk());
        extendedAdDto.setTitle(ad.getTitle());
        extendedAdDto.setDescription(ad.getDescription());
        extendedAdDto.setPrice(ad.getPrice());
        extendedAdDto.setAuthorFirstName(user.getFirstName());
        extendedAdDto.setAuthorLastName(user.getLastName());
        extendedAdDto.setEmail(user.getEmail());
        extendedAdDto.setPhone(user.getPhone());
        extendedAdDto.setImage("/image/" + ad.getImage().getId());
        return extendedAdDto;
    }
}
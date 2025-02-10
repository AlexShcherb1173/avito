package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.ads.Advertisements;
import ru.skypro.homework.dto.ads.AdvertisementsDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdvert;
import ru.skypro.homework.dto.ads.ExtendAdvert;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Archinski
 */
@RequiredArgsConstructor
public class AdMapper {

    private final UserRepository userRepository;

    /**
     * Метод преобразует Dto CreateOrUpdateAd в объект класса Ad.
     *
     * @param createOrUpdateAd Dto, user, image.
     * @return объект класса Ad.
     */
    public Ad createOrUpdateAdToAd(CreateOrUpdateAdvert createOrUpdateAd, User user, Image image) {
        if (createOrUpdateAd == null) {
            throw new IllegalArgumentException("Попытка конвертировать createOrUpdateAd == null");
        }
        Ad newAd = new Ad();

        newAd.setTitle(createOrUpdateAd.getTitle());
        newAd.setPrice(createOrUpdateAd.getPrice());
        newAd.setDescription(createOrUpdateAd.getDescription());
        newAd.setUser(user);
        newAd.setImage(image);

        return newAd;
    }

    /**
     * Метод преобразует Dto CreateOrUpdateAd в объект класса Ad.
     *
     * @param ad, createOrUpdateAd Dto, user, image.
     * @return объект класса Ad.
     */
    public Ad createOrUpdateAdToAd(Ad ad, CreateOrUpdateAdvert createOrUpdateAd) {
        if (createOrUpdateAd == null) {
            throw new IllegalArgumentException("Попытка конвертировать createOrUpdateAd == null");
        }
        Ad newAd = new Ad();

        newAd.setTitle(createOrUpdateAd.getTitle());
        newAd.setPrice(createOrUpdateAd.getPrice());
        newAd.setDescription(createOrUpdateAd.getDescription());
        newAd.setUser(ad.getUser());
        newAd.setImage(ad.getImage());
        newAd.setId(ad.getId());

        return newAd;
    }

    /**
     * Метод преобразует объект класса Ad в Dto AdDTO.
     *
     * @param ad объект класса Ad.
     * @return Dto AdDTO.
     */
    public AdvertisementsDTO adToAdDto(Ad ad) {
        if (ad == null) {
            throw new IllegalArgumentException("Попытка конвертировать ad == null");
        }
        AdvertisementsDTO adDTO = new AdvertisementsDTO();

        adDTO.setPk(ad.getId());
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(ad.getPrice());
        adDTO.setImage(ad.getImage().getImagePath());
        adDTO.setAuthor(ad.getUser().getId());
        return adDTO;
    }

    /**
     * Метод преобразует объект класса Ad в Dto ExtendedAd.
     *
     * @param ad объект класса Ad.
     * @return Dto ExtendedAd.
     */
    public ExtendAdvert adToExtendedDtoOut(Ad ad) {
        if (ad == null) {
            throw new IllegalArgumentException("Попытка конвертировать ad == null");
        }
        ExtendAdvert extendedAd = new ExtendAdvert();

        extendedAd.setPk(ad.getId());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setImage(ad.getImage().getImagePath());

        extendedAd.setAuthorFirstName(ad.getUser().getFirstname());
        extendedAd.setAuthorLastName(ad.getUser().getLastname());
        extendedAd.setEmail(ad.getUser().getPassword());
        extendedAd.setPhone(ad.getUser().getPhone());

        return extendedAd;
    }


    public Advertisements adsToAdsDto(List<Ad> ads) {
        Advertisements adsDTO = new Advertisements();
        adsDTO.setCount(ads.size());
        adsDTO.setResults(ads.stream().map(e -> adToAdDto(e)).collect(Collectors.toList()));
        return adsDTO;
    }
}

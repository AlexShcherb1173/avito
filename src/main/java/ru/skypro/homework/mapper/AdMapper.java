package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.ads.AdvertisementsDTO;
import ru.skypro.homework.dto.ads.ExtendAdvert;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

/**
 * @author Archinski
 */
@RequiredArgsConstructor
public class AdMapper {
    private final UserRepository userRepository;
    /**
     * Метод преобразует Dto AdvertisementsDTO в объект класса Ad.
     * @param AdvertisementsDTO Dto AdDTO.
     * @return объект класса Ad.
     */
    public Ad adDtoToAd(AdvertisementsDTO adDto, ExtendAdvert extendAdvert) {
        Ad newAd = new Ad();

        newAd.setId(adDto.getPk());
        newAd.setTitle(adDto.getTitle());
        newAd.setPrice(adDto.getPrice());
        newAd.setImage(adDto.getImage().getBytes());

        newAd.setDescription(extendAdvert.getDescription());
        return newAd;
    }

    /**
     * Метод преобразует объект класса Ad в Dto AdDTO.
     * @param ad объект класса Ad.
     * @return Dto AdDTO.
     */
    public AdvertisementsDTO adToAdDto(Ad ad) {
        AdvertisementsDTO adDTO = new AdvertisementsDTO();

        adDTO.setPk(ad.getId());
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(ad.getPrice());
        adDTO.setImage("/ads/" + ad.getId() + "/image");
        adDTO.setAuthor(ad.getUser().getId());
        return adDTO;
    }

    /**
     * Метод преобразует объект класса Ad в Dto ExtendedAd.
     * @param ad объект класса Ad.
     * @return Dto ExtendedAd.
     */
    public ExtendAdvert toAdExtendedDtoOut(Ad ad, User user) {
        ExtendAdvert extendedAd = new ExtendAdvert();

        extendedAd.setPk(ad.getId());
        extendedAd.setAuthorFirstName(user.getFirstname());
        extendedAd.setAuthorLastName(user.getLastname());
        extendedAd.setEmail(user.getPassword());
        extendedAd.setPhone(user.getPhone());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setImage(ad.getImage().toString());
        return extendedAd;
    }
}

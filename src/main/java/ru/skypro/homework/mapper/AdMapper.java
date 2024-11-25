package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AdMapper {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public AdEntity toAdEntity(CreateOrUpdateAd ad,
                               String filePath,
                               String username) {
        if (ad == null) {
            throw new EntityNotFoundException("Переданный объект Ad is null");
        }
        AdEntity adEntity = new AdEntity();
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setImage(filePath);
        adEntity.setUser(userRepository.findByUsername(username));
        return adEntity;
    }

    public Ad toAdDto(AdEntity adEntity) {
        if (adEntity == null) {
            throw new EntityNotFoundException("Переданный объект AdEntity is null");
        }
        Ad ad = new Ad();
        ad.setAuthor(adEntity.getUser().getId());
        ad.setImage(adEntity.getImage());
        ad.setPk(adEntity.getId());
        ad.setPrice(adEntity.getPrice());
        ad.setTitle(adEntity.getTitle());
        return ad;
    }

    public ExtendedAd toExtendedAd(AdEntity adEntity) {
        if (adEntity == null) {
            throw new EntityNotFoundException("Переданный объект AdEntity is null");
        }
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(adEntity.getUser().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getUser().getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(adEntity.getUser().getUsername());
        extendedAd.setImage(adEntity.getImage());
        extendedAd.setPhone(adEntity.getUser().getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }

}

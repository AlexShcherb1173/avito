package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdMapper {
    public AdDTO adToAdDTO(Ad ad) {
        return new AdDTO(
                ad.getAuthor().getId(),
                "/image/" + ad.getImage().getId(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public Ad createOrUpdateAdToAd(CreateOrUpdateAdDTO dto, UserEntity author) {
        return new Ad(author, dto.getTitle(), dto.getPrice(), dto.getDescription());
    }

    public ExtendedAdDTO adToExtendedAdDTO(Ad ad) {
        UserEntity author = ad.getAuthor();
        Image image = ad.getImage();
        return new ExtendedAdDTO(ad.getPk(),
                author.getFirstName(),
                author.getLastName(),
                ad.getDescription(),
                author.getEmail(),
                "/image/" + image.getId(),
                author.getPhone(),
                ad.getPrice(),
                ad.getTitle()
        );
    }
}

package ru.skypro.homework.service.impl.AdvertisementService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Advertisement.AdDTO;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Advertisement.ExtendedAd;
import ru.skypro.homework.entity.Advertisement;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserService.UserMapper;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final UserMapper userMapper;

    // Advertisement → AdDTO (для списка объявлений)
    public AdDTO toAdDTO(Advertisement ad) {
        return AdDTO.builder()
                .author(ad.getAuthor().getId())
                .image("/ads/image/" + ad.getId())  // Пример пути
                .pk(ad.getId())
                .price(ad.getPrice())
                .title(ad.getTitle())
                .build();
    }

    // Advertisement → ExtendedAd (полная информация)
    public ExtendedAd toExtendedAd(Advertisement ad) {
        return ExtendedAd.builder()
                .pk(ad.getId())
                .authorFirstName(ad.getAuthor().getFirstName())
                .authorLastName(ad.getAuthor().getLastName())
                .description(ad.getDescription())
                .email(ad.getAuthor().getEmail())
                .image("/ads/image/" + ad.getId())
                .phone(ad.getAuthor().getPhone())
                .price(ad.getPrice())
                .title(ad.getTitle())
                .build();
    }

    // CreateOrUpdateAd → Advertisement (для создания/обновления)
    public Advertisement toAdvertisement(CreateOrUpdateAd dto, User author) {
        return Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .author(author)
                .build();
    }
}
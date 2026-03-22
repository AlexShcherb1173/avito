package ru.avito.mapper;

import org.springframework.stereotype.Component;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.entity.Ad;

@Component
public class AdMapper {

    public AdDto toDto(Ad ad) {
        return new AdDto(
                ad.getId(),
                ad.getAuthor().getId(),
                ad.getTitle(),
                ad.getPrice(),
                ad.getImage()
        );
    }

    public ExtendedAdDto toExtendedDto(Ad ad) {
        return new ExtendedAdDto(
                ad.getId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getPrice(),
                ad.getImage(),
                ad.getAuthor().getId(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getAuthor().getEmail(),
                ad.getAuthor().getPhone()
        );
    }
}
package ru.skypro.homework.mappers;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdMapper {
    public AdDto toAdDto(AdEntity ad) {
        AdDto dto = new AdDto();
        dto.setPk(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setImage(ad.getImage());
        dto.setAuthor(ad.getAuthor().getId());
        return dto;
    }

    public AdsDto adsDto(List<AdEntity> adEntities) {
        AdsDto dto = new AdsDto();
        dto.setCount(adEntities.size());
        List<AdDto> list = new ArrayList<>();
        for (AdEntity adEntity : adEntities) {
            AdDto adDto = toAdDto(adEntity);
            list.add(adDto);
        }
        dto.setResults(list);
        return dto;
    }

    public AdEntity toAdEntity(CreateOrUpdateAdDto dto, UserEntity user) {
        AdEntity ad = new AdEntity();
        ad.setTitle(dto.getTitle());
        ad.setPrice(Integer.parseInt(dto.getPrice()));
        ad.setAuthor(user);
        ad.setDescription(dto.getDescription());
        return ad;
    }

    public AdEntity toUpdateAdEntity(CreateOrUpdateAdDto dto,AdEntity ad) {
        ad.setTitle(dto.getTitle());
        ad.setPrice(Integer.parseInt(dto.getPrice()));
        ad.setDescription(dto.getDescription());
        return ad;
    }

    public ExtendedAdDto toExtendedAdDto(AdEntity ad) {
        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDescription());
        dto.setImage(ad.getImage());
        dto.setAuthorFirstName(ad.getAuthor().getFirstName());
        dto.setAuthorLastName(ad.getAuthor().getLastName());
        dto.setEmail(ad.getAuthor().getEmail());
        dto.setPhone(ad.getAuthor().getPhone());
        return dto;
    }
}

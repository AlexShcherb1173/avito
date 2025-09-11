package ru.skypro.homework.mapper;



import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.User;
import java.time.LocalDateTime;

@Component
public final class AdMapper {
    private AdMapper() {}

    public static AdDto toDto(Ad ad) {
        if (ad == null) {
            return null;
        }

        AdDto dto = new AdDto();
        dto.setAuthor(ad.getAuthor() != null ? ad.getAuthor().getId().intValue() : null);
        dto.setImage(ad.getImageUrl() != null ? "/ads/" + ad.getId() + "/image" : null);
        dto.setPk(ad.getId() == null ? null : ad.getId().intValue());
        dto.setPrice(ad.getPrice());
        dto.setTitle(ad.getTitle());
        return dto;
    }


    public static Ad toEntity(AdDto dto, User author) {
        if (dto == null) {
            return null;
        }

        Ad ad = new Ad();
        if (dto.getPk() != null) {
            ad.setId(dto.getPk().longValue());
        }
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setImageUrl(dto.getImage());
        ad.setAuthor(author);
        if (ad.getCreatedAt() == null) {
            ad.setCreatedAt(LocalDateTime.now());
        }
        return ad;
    }


    public static void updateEntity(Ad ad, AdDto dto) {
        if (ad == null || dto == null) return;
        if (ad == null || dto == null) {
            return;
        }
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setImageUrl(dto.getImage());
    }
}
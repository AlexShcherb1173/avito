package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.model.AdEntity;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final UserMapper userMapper;
    private final FileStorageConfig fileStorageConfig;

    public AdDto toDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        AdDto dto = new AdDto();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setPk(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        // URL формата /ads/image/{id}
        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            dto.setImage(fileStorageConfig.getBaseUrl() + "/ads/image/" + entity.getId());
        } else {
            dto.setImage(null);
        }

        return dto;
    }

    public ExtendedAdDto toExtendedAdDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        // URL формата /ads/image/{id}
        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            dto.setImage(fileStorageConfig.getBaseUrl() + "/ads/image/" + entity.getId());
        } else {
            dto.setImage(null);
        }

        // Данные автора
        if (entity.getAuthor() != null) {
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorLastName(entity.getAuthor().getLastName());
            dto.setEmail(entity.getAuthor().getEmail());
            dto.setPhone(entity.getAuthor().getPhone());
        }

        return dto;
    }

    public AdEntity toEntity(CreateOrUpdateAdDto dto) {
        if (dto == null) {
            return null;
        }

        AdEntity entity = new AdEntity();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());

        return entity;
    }

    public void updateEntityFromDto(AdEntity entity, CreateOrUpdateAdDto dto) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

}

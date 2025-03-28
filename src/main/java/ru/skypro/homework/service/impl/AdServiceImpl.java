package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repositories.AdRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;



    @Override
    public AdsDto getAllAds() {
        return adMapper.adsDto(adRepository.findAll());
    }

    @Transactional
    @Override
    public AdDto createAd(CreateOrUpdateAdDto adDto, MultipartFile multipartFile, UserDetails details) {
        UserEntity user = userRepository.findByEmail(details.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден"));
        AdEntity entity = adMapper.toAdEntity(adDto, user);
        adRepository.save(entity);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                String filePath = imageService.saveImage(multipartFile, entity.getId(), "ads");
                entity.setImage(filePath);
                adRepository.save(entity);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при сохранении изображения", e);
            }
        }
        return adMapper.toAdDto(entity);
    }

    @Override
    public ExtendedAdDto getAdById(int id) {
        AdEntity ad = adRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Такого объявления нет")
        );
        return adMapper.toExtendedAdDto(ad);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @adRepository.findById(#id)" +
            ".get().author.email == authentication.name")
    public void deleteAd(int id) {
        if (!adRepository.existsById(id)) {
            throw new EntityNotFoundException("Такого объявления нет");
        }
        adRepository.deleteById(id);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN') or @adRepository.findById(#id).get().author.email == authentication.name")
    public AdDto updateAd(int id, CreateOrUpdateAdDto dto) {
        AdEntity ad = adRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Такого объявления нет")
        );
        adMapper.toUpdateAdEntity(dto, ad);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    @Override
    public AdsDto getMyAds(UserDetails user) {
        UserEntity entity = userRepository.findByEmail(user.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден"));
        List<AdEntity> entityList = adRepository.findByAuthorId(entity.getId());
        return adMapper.adsDto(entityList);
    }

    @Override
    public void updateAdImage(int id, MultipartFile image) {
        AdEntity ad = adRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Такого объявления нет")
        );
        try {
            if (ad.getImage() != null) {
                imageService.deleteImage(ad.getImage());
            }
            String filePath = imageService.saveImage(image, id, "ads");
            ad.setImage(filePath);
            adRepository.save(ad);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения", e);

        }
    }
}

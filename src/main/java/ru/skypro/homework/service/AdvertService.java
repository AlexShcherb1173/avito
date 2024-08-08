package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdvertDto;
import ru.skypro.homework.dto.AdvertsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Image;

public interface AdvertService {

    AdvertsDto getAllAdverts();

    AdvertDto createAdvert(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile image);

    ExtendedAdDto getAdvertInfo(Long id);

    Void deleteAdvert(Long id);

    AdvertDto updateAdvert(Long id, CreateOrUpdateAdDto createOrUpdateAdDto);

    AdvertsDto getAllAdvertsByAuthor();

    Void updateAdvertImage(Long id, MultipartFile image);
}
